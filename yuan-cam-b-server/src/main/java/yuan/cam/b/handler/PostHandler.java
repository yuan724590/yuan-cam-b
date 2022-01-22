package yuan.cam.b.handler;

import com.google.common.base.Throwables;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yuan.cam.b.commons.Constants;
import yuan.cam.b.dto.EsDTO;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.service.ESService;
import yuan.cam.b.vo.ComputerConfigVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@JobHandler(value = "postHandler")
@Component
@Slf4j
public class PostHandler extends IJobHandler implements Serializable {

    /**
     * 页面大小
     */
    private static final int SIZE = 10;

    /**
     * 爬虫的目标地址
     */
    private static final String JD_URL = "https://search.jd.com/Search?keyword={}&enc=utf-8&wq={}&pvid=b570ad3f9c994755bf038b8897c9613b&psort=3&click=0";

    /**
     * 请求http的连接
     */
    private Connection connection = new HttpConnection();

    /**
     * 请求http的超时时间(ms)
     */
    private int TIME_OUT = 10000;

    @Autowired
    private ESService esService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("PostHandler开始进行");
        //获取总数
        int count = getTotal();
        //已执行数量
        int executedQuantity = 0;
        try {
            List<ComputerConfigVO> list = new ArrayList<>();
            //第一次-通过游标进行查询
            String scrollId = esService.firstQueryEsByScroll(list, SIZE);
            //处理数据
            executedQuantity = processingData(list, executedQuantity, count);
            for(;;){
                list = new ArrayList<>();
                //后续-通过游标进行查询
                scrollId = esService.queryEsByScroll(list, scrollId);
                //处理数据
                executedQuantity = processingData(list, executedQuantity, count);
            }
        } catch (Exception e) {
            log.error("PostHandler脚本异常, e:{}", Throwables.getStackTraceAsString(e));
        }
        log.info("PostHandler执行完毕");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据
     */
    private int processingData(List<ComputerConfigVO> list, int executedQuantity, int count){
        GoodsInfoDTO goodsInfoDTO;
        BigDecimal currentPrice;
        for(ComputerConfigVO computerConfig : list){
            try{
                //获取当前价格
                currentPrice = getCurrentPrice(computerConfig);
                if (currentPrice.compareTo(computerConfig.getHistoricalLowestPrice()) < 0) {
                    //当前价格小于存储的最低价格,就进行更新
                    goodsInfoDTO = new GoodsInfoDTO();
                    goodsInfoDTO.setId(computerConfig.getId());
                    goodsInfoDTO.setHistoricalLowestPrice(currentPrice);
                    esService.insertGoods(goodsInfoDTO);
                }
                executedQuantity++;
                log.info("PostHandler需要执行的总数为:{}, 已处理:{}", count, executedQuantity);
            }catch (Exception e){
                log.error("PostHandler处理http返回数据时异常, e:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return executedQuantity;
    }

    /**
     * 获取当前价格
     */
    private BigDecimal getCurrentPrice(ComputerConfigVO computerConfig) throws Exception{
        String key = computerConfig.getBrand() + " " + computerConfig.getType() + " " + computerConfig.getGoodsName();
        String url = JD_URL.replaceAll("\\{\\}", URLEncoder.encode(key, Constants.UTF_8));
        connection = connection.url(url).timeout(TIME_OUT);
        Document doc = connection.get();
        //doc获取整个页面的所有数据
        Elements ulList = doc.select("ul[class='gl-warp clearfix']");
        Elements liList = ulList.select("li[class='gl-item']");
        for (int k = 0; k < liList.size() && k < 4; k++) {
            if (!liList.get(k).select("span[class='p-promo-flag']").text().trim().equals("广告")) {
                //过滤掉广告位置
                BigDecimal price = new BigDecimal(liList.get(k).select("div[class='p-price']").select("i").text());
                return computerConfig.getHistoricalLowestPrice().min(price);
            }
        }
        return computerConfig.getHistoricalLowestPrice();
    }

    /**
     * 获取总数
     */
    private Integer getTotal(){
        EsDTO.ESQueryGoodsDTO dto = new EsDTO.ESQueryGoodsDTO();
        dto.setPage(1);
        dto.setSize(1);
        return esService.queryGoods(dto).getTotal();
    }
}
