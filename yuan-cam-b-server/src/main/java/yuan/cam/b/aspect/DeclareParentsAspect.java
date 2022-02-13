package yuan.cam.b.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
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
    public void beforeUserService(CommonParent commonParent) {
        log.info(commonParent.getClass().toString());
        commonParent.doSomething();
    }

}