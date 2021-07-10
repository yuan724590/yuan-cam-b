package yuan.cam.b.annotation;

import java.lang.annotation.*;

/**
 * 声明注解
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {

    String type() default "default";
}