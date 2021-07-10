package yuan.cam.b.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import yuan.cam.b.ContentConst;
import yuan.cam.b.dto.ConfigDTO;
import yuan.cam.b.service.ESService;
import yuan.cam.b.util.EsUtil;
import yuan.cam.b.util.LogUtil;
import yuan.cam.b.vo.ComputerConfigVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


@Slf4j
@Service
public class ESServiceImpl implements ESService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String insertConfig(ConfigDTO configDTO, String qid) {
        if (configDTO == null) {
            return "false,入参为空";
        }
        LogUtil.info("开始进行ES新增", qid);
        try {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(configDTO));
            JSONObject countJson = new JSONObject();
            countJson.put("id", jsonObject.get("id"));
            int count = queryCount(countJson, qid);
            if (count > 0) {
                return "false,es中已存在";
            }
            jsonObject.put("createTime", (int) (System.currentTimeMillis() / 1000));
            jsonObject.put("updateTime", (int) (System.currentTimeMillis() / 1000));
            IndexRequest indexRequest = new IndexRequest(ContentConst.ES_INDEX);
            indexRequest.source(jsonObject, XContentType.JSON);
            EsUtil.getESClient().index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LogUtil.error("请求es异常:" + Throwables.getStackTraceAsString(e), qid);
            return "false,请求es异常";
        }
        return "true";
    }

    @Override
    public String updateConfig(String _id, double floorPrice, String qid) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(ContentConst.ES_INDEX, _id);
            updateRequest.doc(jsonBuilder().startObject().field("floorPrice", floorPrice)
                    .field("updateTime", System.currentTimeMillis() / 1000).endObject());
            EsUtil.getESClient().update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LogUtil.error("请求es异常:" + Throwables.getStackTraceAsString(e), qid);
            return "false,请求es异常";
        }
        return "true";
    }

    @Override
    public String deleteConfig(List<Integer> idList, String qid) {
        try {
            LogUtil.debug("开始进行ES删除", qid);
            DeleteByQueryRequest request = new DeleteByQueryRequest(ContentConst.ES_INDEX);
            request.setConflicts("proceed");
            request.setQuery(new BoolQueryBuilder().filter(new TermsQueryBuilder("id", idList)));
            request.setTimeout(TimeValue.timeValueMinutes(2));
            request.setScroll(TimeValue.timeValueMinutes(10));
            request.setRefresh(true);
            BulkByScrollResponse bulkResponse = EsUtil.getESClient().deleteByQuery(request, RequestOptions.DEFAULT);
            if (bulkResponse.getStatus().getTotal() != idList.size()) {
                return "false,es删除数据异常";
            }
        } catch (Exception e) {
            LogUtil.error("请求es异常:" + Throwables.getStackTraceAsString(e), qid);
            return "false,请求es删除数据异常";
        }
        return "true";
    }

    @Override
    public List<ComputerConfigVO> queryConfig(JSONObject jsonObject, Integer page, Integer size, String qid) {
        try {
            LogUtil.debug("开始进行ES查询", qid);
            int from = (page - 1) * size;
            String key = jsonObject.toString() + page + size;
            if (redisTemplate.opsForValue().get(key) == null) {
                MultiSearchRequest request = new MultiSearchRequest();
                SearchRequest searchRequest = new SearchRequest(ContentConst.ES_INDEX);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                if (jsonObject.size() > 0) {
                    searchSourceBuilder.query(QueryBuilders.matchQuery(jsonObject.keySet().iterator().next(), jsonObject.get(jsonObject.keySet().iterator().next())));
                }
                searchSourceBuilder.from(from);
                searchSourceBuilder.size(size);
                searchRequest.source(searchSourceBuilder);
                request.add(searchRequest);
                MultiSearchResponse searchResponse = EsUtil.getESClient().msearch(request, RequestOptions.DEFAULT);
                if (searchResponse == null || searchResponse.getResponses() == null || searchResponse.getResponses().length == 0) {
                    return Collections.emptyList();
                }
                MultiSearchResponse.Item item = searchResponse.getResponses()[0];
                if (item != null && item.getResponse() != null && item.getResponse().getHits() != null) {
                    List<ComputerConfigVO> list = new ArrayList<>();
                    for (SearchHit hit : item.getResponse().getHits()) {
                        ComputerConfigVO computerConfig = JSONObject.parseObject(String.valueOf(hit.getSourceAsMap()), ComputerConfigVO.class);
                        computerConfig.setEsId(Integer.valueOf(hit.getId()));
                        list.add(computerConfig);
                    }
                    redisTemplate.opsForValue().set(key, list, 3600, TimeUnit.SECONDS);
                    return list;
                }
            }
            return (List<ComputerConfigVO>) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            LogUtil.error("请求es异常:" + Throwables.getStackTraceAsString(e), qid);
        }
        return Collections.emptyList();
    }

    @Override
    public Integer queryCount(JSONObject jsonObject, String qid) {
        int count;
        try {
            CountRequest countRequest = new CountRequest();
            countRequest.indices(ContentConst.ES_INDEX);
            if (jsonObject.size() > 0) {
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(QueryBuilders.matchQuery(jsonObject.keySet().iterator().next(), jsonObject.get(jsonObject.keySet().iterator().next())));
                countRequest.source(searchSourceBuilder);
            }
            CountResponse countResponse = EsUtil.getESClient().count(countRequest, RequestOptions.DEFAULT);
            count = (int) countResponse.getCount();
        } catch (Exception e) {
            LogUtil.error("查询总数请求es异常:" + Throwables.getStackTraceAsString(e), qid);
            return 0;
        }
        return count;
    }
}
