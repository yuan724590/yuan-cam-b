package yuan.cam.bb.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class testAspect {

    /**
     * 切入点设置
     */
    @Pointcut("@annotation(yuan.cam.bb.annotation.TestAnnotation)")
    public void entryPoint() {
        // 无需内容
    }

    /**
     * 在切入点之前做的事
     * 入参JoinPoint
     */
    //根据注解进行aop
    @Before("entryPoint() && args(object)")
    public void interfaceBefore(Object object){
        System.out.println("interfaceBefore-----");
    }

    //其余情况同理，可以通过一个注解处理多种情况
}