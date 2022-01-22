package yuan.cam.b.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page<T> {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 结果
     */
    private T result;
}
