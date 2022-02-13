package yuan.cam.b.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.RandomScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import yuan.cam.b.commons.Constants;
import yuan.cam.b.commons.EsCommonService;
import yuan.cam.b.dto.EsDTO;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.exception.BusinessException;
import yuan.cam.b.exception.GoodsExceptionEnum;
import yuan.cam.b.service.ESService;
import yuan.cam.b.vo.BrandCountVO;
import yuan.cam.b.vo.ComputerConfigVO;
import yuan.cam.b.vo.Page;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class ESServiceImpl implements ESService {

    @Resource
    private EsCommonService esCommonService;

    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public String insertGoods(GoodsInfoDTO dto) {
        try {
            dto.setCreateTime(LocalDateTime.now().getSecond());
            dto.setUpdateTime(LocalDateTime.now().getSecond());
            dto.setDeleted(Constants.NOT_DELETE_STATUS);
            //新增数据
            esCommonService.insert(Constants.COMPUTER_CONFIG_INDEX, dto);
        } catch (Exception e) {
            log.error("请求es异常:{}", Throwables.getStackTraceAsString(e));
            throw new BusinessException(GoodsExceptionEnum.GOODS_ALREADY_EXISTS);
        }
        return "添加成功";
    }

    @Override
    public String deleteGoods(List<Integer> idList) {
        if(CollectionUtils.isEmpty(idList)){
            return "更新成功";
        }
        try {
            UpdateByQueryRequest request = new UpdateByQueryRequest();
            request.setQuery(QueryBuilders.termsQuery(Constants._ID, idList));
            request.setBatchSize(idList.size());
            request.setTimeout(TimeValue.timeValueSeconds(2));
            request.indices(Constants.COMPUTER_CONFIG_INDEX);
            request.setScript(new Script(ScriptType.INLINE, "painless", "ctx._source['deleted'] = 1", Collections.emptyMap()));
            restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("请求es异常:{}", Throwables.getStackTraceAsString(e));
        }
        return "更新成功";
    }

    @Override
    public Page<List<ComputerConfigVO>> queryGoods(EsDTO.ESQueryGoodsDTO dto) {
        try {
            String key = JSON.toJSONString(dto) + ":" + dto.getPage() + ":" + dto.getSize();
            if (redisTemplate.opsForValue().get(key) == null) {
                SearchRequest searchRequest = new SearchRequest(Constants.COMPUTER_CONFIG_INDEX);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                //组装查询es的条件
                searchSourceBuilder.query(queryGoodsByEs(dto));
                searchSourceBuilder.from((dto.getPage() - 1) * dto.getSize());
                searchSourceBuilder.size(dto.getSize());
                searchRequest.source(searchSourceBuilder);
                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                //封装es的请求结果
                Page<List<ComputerConfigVO>> page = getListByResponse(searchResponse);
                redisTemplate.opsForValue().set(key, page, 1, TimeUnit.HOURS);
                return page;
            }
        } catch (Exception e) {
            log.error("请求es异常, 入参:{}, e:{}", JSON.toJSONString(dto), Throwables.getStackTraceAsString(e));
        }
        return new Page<>(0, Collections.emptyList());
    }

    /**
     * 封装es的请求结果
     */
    private Page<List<ComputerConfigVO>> getListByResponse(SearchResponse searchResponse){
        if (searchResponse == null) {
            return new Page<>(0, Collections.emptyList());
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<ComputerConfigVO> list = new ArrayList<>(searchHits.length);
        for (SearchHit hit : searchHits) {
            list.add(JSONObject.parseObject(JSONObject.toJSONString(hit.getSourceAsMap()), ComputerConfigVO.class));
        }
        return new Page<>((int) searchResponse.getHits().getTotalHits().value, list);
    }

    /**
     * 组装查询es的条件
     */
    private BoolQueryBuilder queryGoodsByEs(EsDTO.ESQueryGoodsDTO dto){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotEmpty(dto.getGoodsName())){
            boolQueryBuilder.must(QueryBuilders.matchQuery(Constants.GOODS_NAME, dto.getGoodsName()));
        }
        if(CollectionUtils.isNotEmpty(dto.getIdList())){
            boolQueryBuilder.must(QueryBuilders.termsQuery(Constants._ID, dto.getIdList()));
        }
        boolQueryBuilder.must(QueryBuilders.matchQuery(Constants.DELETED, Constants.NOT_DELETE_STATUS));
        return boolQueryBuilder;
    }

    @Override
    public List<ComputerConfigVO> queryByScroll(){
        List<ComputerConfigVO> list = new ArrayList<>();
        //第一次通过游标进行查询
        String scrollId = firstQueryEsByScroll(list, 2);
        for(;;){
            //后续-通过游标进行查询
            scrollId = queryEsByScroll(list, scrollId);
            if(StringUtils.isEmpty(scrollId)){
                break;
            }
        }
        return list;
    }

    @Override
    public String queryEsByScroll(List<ComputerConfigVO> list, String scrollId){
        try {
            //通过设置所需的scrollId来创建搜索scroll请求
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(5));
            SearchResponse searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
            //封装es的请求结果
            List<ComputerConfigVO> configList = getListByResponse(searchResponse).getResult();
            if(CollectionUtils.isNotEmpty(configList)){
                list.addAll(configList);
                return searchResponse.getScrollId();
            }
        } catch (Exception e) {
            log.error("queryByScroll-请求通过游标查询es失败, e:{}", Throwables.getStackTraceAsString(e));
        }
        return "";
    }

    @Override
    public String firstQueryEsByScroll(List<ComputerConfigVO> list, int size){
        try{
            SearchRequest searchRequest = new SearchRequest(Constants.COMPUTER_CONFIG_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(size);
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(TimeValue.timeValueSeconds(5));
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //封装es的请求结果
            list.addAll(getListByResponse(searchResponse).getResult());
            //读取返回的scrollId，在下次搜索scroll调用中将需要它
            return searchResponse.getScrollId();
        }catch (Exception e){
            log.error("firstQueryEsByScroll-请求es异常, e:{}", Throwables.getStackTraceAsString(e));
        }
        return "";
    }

    @Override
    public Boolean queryIsExistById(Integer id) {
        //通过id查询是否存在
        return esCommonService.queryIsExistById(Constants.COMPUTER_CONFIG_INDEX, id);
    }

    @Override
    public BrandCountVO queryGoodsCount(){
        try{
            SearchRequest searchRequest = new SearchRequest(Constants.COMPUTER_CONFIG_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery(Constants.DELETED, Constants.NOT_DELETE_STATUS));
            searchRequest.source(searchSourceBuilder);
            String termsName = UUID.randomUUID().toString();
            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(termsName).field(Constants.GOODS_BRAND);
            TopHitsAggregationBuilder topHitsAggregationBuilder = AggregationBuilders.topHits("goodsBrandHits")
                    .size(1)
                    .sort(Constants.CREATE_TIME, SortOrder.DESC)
                    .fetchSource(new String[]{Constants.ID}, null);
            aggregationBuilder.subAggregation(topHitsAggregationBuilder);
            searchSourceBuilder.aggregation(aggregationBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //组装聚合数据
            return assemblyData(searchResponse, termsName);
        }catch (Exception e){
            log.error("firstQueryEsByScroll-请求es异常, e:{}", Throwables.getStackTraceAsString(e));
        }
        return new BrandCountVO();
    }

    /**
     * 组装聚合数据
     */
    private BrandCountVO assemblyData(SearchResponse searchResponse, String termsName){
        Terms terms = searchResponse.getAggregations().get(termsName);
        List<? extends Terms.Bucket> list = terms.getBuckets();
        List<BrandCountVO.BrandCountInfoVO> infoList = new ArrayList<>();
        for(Terms.Bucket bucket : list){
            infoList.add(new BrandCountVO.BrandCountInfoVO(bucket.getKey().toString(), (int)bucket.getDocCount()));
        }
        return new BrandCountVO(infoList);
    }

    @Override
    public Page<List<ComputerConfigVO>> randomAcquisitionGoods(Integer page, Integer size){
        try {
            SearchRequest searchRequest = new SearchRequest(Constants.COMPUTER_CONFIG_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //组装查询es的条件
            MatchPhraseQueryBuilder matchPhraseQueryBuilder = new MatchPhraseQueryBuilder(Constants.GOODS_NAME, "英特尔");
            //设置根据_seq_no字段(版本号,没更新一次自增1)进行随机, 不设置时默认为_id字段, 设置这个function的权重是23
            RandomScoreFunctionBuilder randomScoreFunctionBuilder
                    = ScoreFunctionBuilders.randomFunction().seed(System.currentTimeMillis()).setField("_seq_no").setWeight(23);
            //设置随机数据的查询条件, 本质上此处随机无意义
            TermQueryBuilder termQueryBuilder1 = new TermQueryBuilder(Constants.DELETED, Constants.NOT_DELETE_STATUS);
            FunctionScoreQueryBuilder.FilterFunctionBuilder filterFunctionBuilder
                    = new FunctionScoreQueryBuilder.FilterFunctionBuilder(termQueryBuilder1, randomScoreFunctionBuilder);
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{filterFunctionBuilder};
            FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(matchPhraseQueryBuilder, filterFunctionBuilders);

            searchSourceBuilder.query(functionScoreQueryBuilder);
            searchSourceBuilder.from((page - 1) * size);
            searchSourceBuilder.size(size);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //封装es的请求结果
            return getListByResponse(searchResponse);
        } catch (Exception e) {
            log.error("请求es异常, 入参page:{}, size:{}, e:{}", page, size, Throwables.getStackTraceAsString(e));
        }
        return new Page<>(0, Collections.emptyList());
    }
}
