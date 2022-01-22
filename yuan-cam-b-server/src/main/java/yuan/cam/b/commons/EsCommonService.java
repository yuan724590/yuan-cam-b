package yuan.cam.b.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class EsCommonService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 通过id查询是否存在
     */
    public Boolean queryIsExistById(String index, Integer id){
        if(id == null || id < 0){
            return false;
        }
        try {
            CountRequest countRequest = new CountRequest();
            countRequest.indices(index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery(Constants._ID, id));
            countRequest.source(searchSourceBuilder);
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            return (int) countResponse.getCount() > 0;
        } catch (Exception e) {
            log.error("queryIsExistById-请求es异常, index:{}, id:{}, e:{}", index, id, Throwables.getStackTraceAsString(e));
        }
        return false;
    }

    /**
     * 新增数据
     */
    public void insert(String index, Object object){
        if(object == null){
            return;
        }
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(object));
        if(jsonObject.isEmpty()){
            return;
        }
        try {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(jsonObject, XContentType.JSON);
            if(jsonObject.containsKey(Constants.ID)){
                indexRequest.id(jsonObject.getString(Constants.ID));
            }
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("请求es进行新增失败, index:{}, 入参:{}, e:{}", index, jsonObject.toJSONString(), Throwables.getStackTraceAsString(e));
        }
    }
}
