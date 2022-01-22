package yuan.cam.b.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    public BusinessException(ExceptionInterface exceptionInterface){
        super(exceptionInterface.getErrorMessage());
        this.errorCode = exceptionInterface.getErrorCode();
        this.errorMessage = exceptionInterface.getErrorMessage();
    }
}