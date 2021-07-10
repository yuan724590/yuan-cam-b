package yuan.cam.b.vo;

import lombok.Data;

@Data
public class ComputerConfigVO {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 主键id
     */
    private Integer esId;

    /**
     * 商品名称
     */
    private String brand;

    /**
     * 商品类型
     */
    private String type;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 底价
     */
    private Double floorPrice;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 更新时间
     */
    private Integer updateTime;
}
