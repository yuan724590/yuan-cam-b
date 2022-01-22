package yuan.cam.b.exception;

public interface ExceptionInterface {

    /**
     * 获取错误码
     */
    Integer getErrorCode();

    /**
     * 获取错误信息
     */
    String getErrorMessage();
}