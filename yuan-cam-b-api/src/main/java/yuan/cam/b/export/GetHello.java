package yuan.cam.b.export;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yuan.cam.b.common.Constants;
import yuan.cam.b.dto.HelloDTO;
import yuan.cam.b.vo.ResultVO;

@Api(value = "测试")
@FeignClient(value = Constants.SERVICE_NAME)
public interface GetHello {

    @PostMapping("/redis")
    String sayHello(@RequestBody @Validated HelloDTO.SayHelloDTO reqDTO);

    @GetMapping("/hello")
    String sayHelloAgain();

    @GetMapping("/helloHystrix")
    String sayHelloHystrix();

    @PostMapping(value = "/hello/a", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO helloA(@RequestBody @Validated HelloDTO.HelloADTO reqDTO);

    @PostMapping(value = "/hello/b", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO helloB(@RequestBody @Validated HelloDTO.HelloBDTO reqDTO);

    @PostMapping(value = "/hello/fanout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void helloFanout(@RequestBody @Validated HelloDTO.HelloFanoutDTO reqDTO);

    @PostMapping(value = "/hello/direct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void helloDirect(@RequestBody @Validated HelloDTO.HelloDirectDTO reqDTO);

    @PostMapping(value = "/hello/topic", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void helloTopic(@RequestBody @Validated HelloDTO.HelloTopicDTO reqDTO);

    @GetMapping("/declare")
    String declare();
}
