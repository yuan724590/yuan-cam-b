package yuan.cam.b.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> {

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
    private T result;
}
