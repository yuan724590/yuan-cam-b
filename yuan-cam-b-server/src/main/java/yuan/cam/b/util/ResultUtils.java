package yuan.cam.b.util;

import org.springframework.stereotype.Component;
import yuan.cam.b.commons.Constants;
import yuan.cam.b.vo.ResultVO;

@Component
public class ResultUtils {

    public static<T> ResultVO<T> data(T t) {
        return new ResultVO<>(Constants.SUCCESS_CODE, Constants.SUCCESS, t);
    }
}
