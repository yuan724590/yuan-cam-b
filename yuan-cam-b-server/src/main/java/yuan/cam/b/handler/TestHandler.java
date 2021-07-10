package yuan.cam.b.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@JobHandler(value = "TestHandler")
@Component
public class TestHandler extends IJobHandler implements Serializable {

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        System.out.println("aaaaa");
        return ReturnT.SUCCESS;
    }
}
