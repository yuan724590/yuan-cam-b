package yuan.cam.bb.Controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuan.cam.bb.dto.HelloDTO;
import yuan.cam.bb.export.GetHello;
import yuan.cam.bb.service.HelloService;
import yuan.cam.bb.vo.ResultVO;

@RequestMapping(value="/hello")
@RestController
public class HelloController implements GetHello{

    @Autowired
    private HelloService helloService;

    @Override
    public String sayHello(@RequestBody @Validated HelloDTO.SayHelloDTO reqDTO) {
        return helloService.sayHello(reqDTO.getName());
    }

    @Override
    public String sayHelloAgain() {
        return helloService.sayHelloAgain();
    }

    @Override
    @HystrixCommand(fallbackMethod = "hiErr")
    public String sayHelloHystrix() {
        return helloService.sayHelloHystrix();
    }

    @Override
    public ResultVO helloA(@RequestBody @Validated HelloDTO.HelloADTO reqDTO){
        String result = helloService.helloA(reqDTO.getPage());
        return new ResultVO(result);
    }

    @Override
    public ResultVO helloB(@RequestBody @Validated HelloDTO.HelloBDTO reqDTO){
        String result = helloService.rest(reqDTO.getStr());
        return new ResultVO(result);
    }

    @Override
    public void helloFanout(@RequestBody @Validated HelloDTO.HelloFanoutDTO reqDTO){
        helloService.helloFanout(reqDTO.getMessage());
    }

    @Override
    public void helloDirect(@RequestBody @Validated HelloDTO.HelloDirectDTO reqDTO){
        helloService.helloDirect(reqDTO.getMessage());
    }

    @Override
    public void helloTopic(@RequestBody @Validated HelloDTO.HelloTopicDTO reqDTO){
        helloService.helloTopic(reqDTO.getMessage());
    }

    private String hiErr(){
        return "sorry,have a error";
    }
}
