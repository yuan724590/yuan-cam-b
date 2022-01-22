package yuan.cam.b.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "computer_config")
public class ComputerConfig {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 商品品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 商品类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品价格
     */
    @Column(name = "price")
    private Integer price;

    /**
     * 底价
     */
    @Column(name = "floor_Price")
    private Integer floorPrice;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Integer updateTime;
}
