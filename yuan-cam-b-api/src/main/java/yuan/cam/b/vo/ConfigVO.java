package yuan.cam.b.vo;

import lombok.Data;

@Data
public class ConfigVO {

    private Integer id;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品类型
     */
    private String type;

    /**
     * 商品名
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

}
