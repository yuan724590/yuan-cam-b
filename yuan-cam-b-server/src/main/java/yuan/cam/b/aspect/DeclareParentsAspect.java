package yuan.cam.b.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import yuan.cam.b.service.CommonParent;
import yuan.cam.b.service.impl.CommonParentImpl;

@Aspect
@Component
@Slf4j
public class DeclareParentsAspect {

    //声明了service包中的类都会引入一个CommonParent父接口，并用CommonParentImpl实现，形成一个代理对象commonParent
    @DeclareParents(value="yuan.cam.b.service.impl.HelloServiceImpl", defaultImpl= CommonParentImpl.class)
    private CommonParent parent;
    
    
    @Before(value = "execution (* yuan.cam.b.service.HelloService.sayHelloAgain()) && this(commonParent)", argNames = "commonParent")
    public void before(CommonParent commonParent) {
        log.info(commonParent.getClass().toString());
        commonParent.doSomething();
    }

    //会将源class传过来, 当前指得是yuan.cam.b.service.impl.HelloServiceImpl
    @After(value = "execution (* yuan.cam.b.service.HelloService.sayHelloAgain()) && target(object)")
    public void after(Object object) {
        log.info(object.toString());
    }

    @After(value = "execution (* yuan.cam.b.service.HelloService.rest(..)) ")
    public void afterReturning(JoinPoint point) {
        //返回方法参数
        log.info(JSON.toJSONString(point.getArgs()));
        //返回代理对象
        log.info(JSON.toJSONString(point.getThis()));
        //返回目标对象
        log.info(JSON.toJSONString(point.getTarget()));
        //返回正在被通知的方法相关信息
        log.info(JSON.toJSONString(point.getSignature()));
    }
}