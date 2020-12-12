package yuan.cam.bb.vo;

import lombok.Data;

@Data
public class ResultVO {

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 状态信息
     */
    private String statusMsg;

    /**
     * 结果
     */
    private Object result;

    public ResultVO(Integer statusCode, String statusMsg, Object result){
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.result = result;
    }

    public ResultVO(Object result){
        this.statusCode = 0;
        this.statusMsg = "success";
        this.result = result;
    }
}
