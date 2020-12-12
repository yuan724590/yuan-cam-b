package yuan.cam.bb.util;

import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.cam.bb.vo.ResultVO;

import java.util.List;

@ControllerAdvice
public class ExceptionUtil {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO exceptionHandler(MethodArgumentNotValidException  e)
    {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errorList = bindingResult.getAllErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < errorList.size(); i++){
            String[] codes = errorList.get(i).getCodes();
            if(codes == null || codes.length == 0){
                return new ResultVO(500, "参数异常", e.toString());
            }
            String[] fields = codes[0].split("\\.");
            stringBuilder.append(fields[1]);
            stringBuilder.append(".");
            stringBuilder.append(fields[2]);
            stringBuilder.append(errorList.get(i).getDefaultMessage());
            if(i + 1 < errorList.size()){
                stringBuilder.append(";");
            }
        }
        return new ResultVO(500, "参数异常", stringBuilder.toString());
    }
}
