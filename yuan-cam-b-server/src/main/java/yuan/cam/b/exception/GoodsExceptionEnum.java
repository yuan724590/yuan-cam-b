package yuan.cam.b.exception;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GoodsExceptionEnum implements ExceptionInterface{

    /**
     * 商品异常类的枚举
     */

    GOODS_ALREADY_EXISTS(100, "商品已存在"),

    ;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 内容
     */
    private String errorMessage;

    @Override
    public Integer getErrorCode() {

        return errorCode;
    }

    @Override
    public String getErrorMessage() {

        return errorMessage;
    }
}
