package yuan.cam.bb.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EsUtil {

    private static RestHighLevelClient client;

    @Value("${esHost}")
    private String esHost;

    @Value("${esPort}")
    private int port;

    @Value("${esSheme}")
    private String scheme;

    private static String esHostCopy;

    private static int portCopy;

    private static String schemeCopy;

    @PostConstruct
    public void init(){
        esHostCopy = esHost;
        portCopy = port;
        schemeCopy = scheme;
    }

    public static RestHighLevelClient getESClient(){
        if(client == null){
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(esHostCopy, portCopy, schemeCopy)));
        }
        return client;
    }

    public void shutdown(){
        if(client != null){
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
