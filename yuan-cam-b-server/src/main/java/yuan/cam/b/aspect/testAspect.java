package yuan.cam.b.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import yuan.cam.b.annotation.TestAnnotation;
import yuan.cam.b.vo.ResultVO;

@Aspect
@Component
public class testAspect {

    /**
     * 切入点设置
     */
    @Pointcut("@annotation(yuan.cam.b.annotation.TestAnnotation)")
    public void entryPoint() {
        // 无需内容
    }

    /**
     * 在切入点之前做的事
     * 入参JoinPoint
     * 根据注解进行aop
     */
    @Before("entryPoint() && args(object) && @annotation(testAnnotation)")
    public void interfaceBefore(Object object, TestAnnotation testAnnotation) {

        System.out.println("interfaceBefore-----" + testAnnotation.type());
    }

    //其余情况同理，可以通过一个注解处理多种情况

    /**
     * 在切入点之后做的事
     */
    @After("entryPoint()")
    public void interfaceAfter() {
    }

    /**
     * 在return之后做的事
     */
    @AfterReturning(returning = "result", value = "entryPoint()")
    public ResultVO doAfterReturning(JoinPoint joinPoint, Object result) {
        return null;
    }

    /**
     * 更改返回值,不能改返回值类型,会与@AfterReturning注解冲突,不用就注释
     */
//    @Around("entryPoint()")
//    public ResultVO interfaceAround(ProceedingJoinPoint point) throws Throwable {
//        Object result = point.proceed();
//        return new ResultVO(0, "", result);
//    }
}