package yuan.cam.b.commons;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yuan.cam.b.exception.BusinessException;
import yuan.cam.b.vo.ResultVO;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errorList = bindingResult.getAllErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < errorList.size(); i++) {
            String[] codes = errorList.get(i).getCodes();
            if (codes == null || codes.length == 0) {
                return new ResultVO(500, "参数异常", e.toString());
            }
            String[] fields = codes[0].split("\\.");
            stringBuilder.append(fields[1]);
            stringBuilder.append(".");
            stringBuilder.append(fields[2]);
            stringBuilder.append(errorList.get(i).getDefaultMessage());
            if (i + 1 < errorList.size()) {
                stringBuilder.append(";");
            }
        }
        return new ResultVO(500, "参数异常", stringBuilder.toString());
    }

    /**
     * 兜底的捕获
     */
    @ExceptionHandler(Throwable.class)
    public ResultVO throwableHandler(BusinessException exception){
        log.error("Internal Server Error, e:{}", Throwables.getStackTraceAsString(exception));
        return new ResultVO<>(exception.getErrorCode(), exception.getErrorMessage(), null);
    }
}
