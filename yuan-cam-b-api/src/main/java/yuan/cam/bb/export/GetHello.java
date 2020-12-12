package yuan.cam.bb.export;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yuan.cam.bb.ContentConst;
import yuan.cam.bb.dto.HelloDTO;
import yuan.cam.bb.vo.ResultVO;

@Api(value = "测试")
@FeignClient(value = ContentConst.SERVICE_NAME)
public interface GetHello {

    @PostMapping("/redis")
    String sayHello(@RequestBody @Validated HelloDTO.SayHelloDTO reqDTO);

    @GetMapping("/hello")
    String sayHelloAgain();

    @GetMapping("/helloHystrix")
    String sayHelloHystrix();

    @PostMapping(value = "/hello/a", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    ResultVO helloA(@RequestBody @Validated HelloDTO.HelloADTO reqDTO);

    @PostMapping(value = "/hello/b", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    ResultVO helloB(@RequestBody @Validated HelloDTO.HelloBDTO reqDTO);

    @PostMapping(value = "/hello/fanout", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    void helloFanout(@RequestBody @Validated HelloDTO.HelloFanoutDTO reqDTO);

    @PostMapping(value = "/hello/direct", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    void helloDirect(@RequestBody @Validated HelloDTO.HelloDirectDTO reqDTO);

    @PostMapping(value = "/hello/topic", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    void helloTopic(@RequestBody @Validated HelloDTO.HelloTopicDTO reqDTO);
}
