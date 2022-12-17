package yuan.cam.b.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "computer_config")
public class ComputerConfig {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 商品品牌
     */
    @Column(name = "goods_brand")
    private String goodsBrand;

    /**
     * 商品类型 1-CPU 2-主板 3-显卡 4-显示器 5-内存条 6-硬盘
     */
    @Column(name = "goods_type")
    private String goodsType;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品价格
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 商品史低价格
     */
    @Column(name = "historical_lowest_price")
    private BigDecimal historicalLowestPrice;

    /**
     * 图片url
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 图片的高度
     */
    @Column(name = "image_height")
    private Integer imageHeight;

    /**
     * 图片的宽度
     */
    @Column(name = "image_width")
    private Integer imageWidth;

    /**
     * 删除状态 0-未删除 1-已删除
     */
    private Byte deleted;

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
