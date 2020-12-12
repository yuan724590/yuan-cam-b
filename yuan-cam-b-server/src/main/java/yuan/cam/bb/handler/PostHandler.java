package yuan.cam.bb.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yuan.cam.bb.service.ESService;
import yuan.cam.bb.util.LogUtil;
import yuan.cam.bb.vo.ComputerConfigVO;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@JobHandler(value = "PostHandler")
@Component
public class PostHandler extends IJobHandler implements Serializable{

    @Autowired
    private ESService esService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        String qid = UUID.randomUUID().toString();
        LogUtil.debug("PostHandler开始进行", qid);
        String jdUrl = "https://search.jd.com/Search?keyword={}&enc=utf-8&wq={}&pvid=b570ad3f9c994755bf038b8897c9613b&psort=3&click=0";
        int timeOut = 10000;
        int size = 10;
        int count = esService.queryCount(new JSONObject(), qid);
        int page = count % size == 0 ? count / size : count / size + 1;
        try{
            for(int i = 1; i < page + 1; i++){
                List<ComputerConfigVO> list = esService.queryConfig(new JSONObject(), i, size, qid);
                for(int j = 0; j < list.size(); j++){

                    ComputerConfigVO computerConfig = list.get(j);
                    double minPrice = computerConfig.getPrice();
                    String key = computerConfig.getBrand() + " " + computerConfig.getType() + " " + computerConfig.getName();
                    String url = jdUrl.replaceAll("\\{\\}", URLEncoder.encode(key, "UTF-8" ));
                    Connection con = new HttpConnection();
                    Connection connection = con.url(url).timeout(timeOut);
                    Document doc = connection.get();
                    //doc获取整个页面的所有数据
                    Elements ulList = doc.select("ul[class='gl-warp clearfix']");
                    Elements liList = ulList.select("li[class='gl-item']");
                    for (int k = 0; k < liList.size() && k < 4; k++) {
                        //排除广告位置
                        if (!liList.get(k).select("span[class='p-promo-flag']").text().trim().equals("广告")) {
                            Double price = Double.valueOf(liList.get(k).select("div[class='p-price']").select("i").text());//打印商品标题到控制台
                             minPrice = Math.min(minPrice, price);
                        }
                    }
                    if(minPrice < computerConfig.getFloorPrice()){
                        esService.updateConfig(computerConfig.getEsId().toString(), minPrice, qid);
                    }

                }
            }
        }catch (Exception e){
            LogUtil.error("请求http异常" + Throwables.getStackTraceAsString(e), qid);
        }
        return ReturnT.SUCCESS;
    }
}
