package yuan.cam.b.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import yuan.cam.b.vo.ResultVO;

@Aspect
@Component
public class AopUtil {

    /**
     * 切入点设置
     * 1.切入包下所有接口
     * 2.切入文件下所有接口
     * 3.切入项目中所有接口
     */
    @Pointcut("execution(* yuan.cam.b.export.*.*(..))")
//    @Pointcut("execution(public * yuan.cam.b.export.GetHello.*(..))")
//    @Pointcut("execution(* *(..))")
    public void entryPoint() {
        // 无需内容
    }

    /**
     * 在切入点之前做的事
     * 入参JoinPoint
     */
    @Before("entryPoint()")
    //本处写的JoinPoint是上面那种获取参数的例子，可以在interfaceBefore后面括号中换成对应的object，并在args后的括号内声明
//    @Before("entryPoint() && args(joinPoint)")
    public void interfaceBefore(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        if (ArrayUtils.isEmpty(objects)) {
            return;
        }
        System.out.println("interfaceBefore-----");
    }

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