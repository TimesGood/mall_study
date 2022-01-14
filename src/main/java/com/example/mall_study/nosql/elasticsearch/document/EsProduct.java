package com.example.mall_study.nosql.elasticsearch.document;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 搜索中的商品信息
 * 不需要中文分词的字段设置成@Field(type = FieldType.Keyword)类型，
 * 需要中文分词的设置成@Field(analyzer = "ik_max_word",type = FieldType.Text)类型。
 */
@Document(indexName = "pms",shards = 1,replicas = 0)
public class EsProduct implements Serializable {
    private static final long serialVersionUID = -1L;
    @Id
    @ApiModelProperty("商品Id")
    private Long id;
    @Field(type = FieldType.Keyword)
    @ApiModelProperty("商品条码")
    private String productSn;
    @ApiModelProperty("品牌Id")
    private Long brandId;
    @Field(type = FieldType.Keyword)
    @ApiModelProperty("品牌名称")
    private String brandName;
    @ApiModelProperty("商品分类Id")
    private Long productCategoryId;
    @Field(type = FieldType.Keyword)
    @ApiModelProperty("商品分类名")
    private String productCategoryName;
    @ApiModelProperty("商品img地址")
    private String pic;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    @ApiModelProperty("商品名")
    private String name;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    @ApiModelProperty("商品标题")
    private String subTitle;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    @ApiModelProperty("商品Id")
    private String keywords;
    @ApiModelProperty("定价")
    private BigDecimal price;
    @ApiModelProperty("销售数")
    private Integer sale;
    @ApiModelProperty("商品状态")
    private Integer newStatus;
    private Integer recommandStatus;
    @ApiModelProperty("库存")
    private Integer stock;
    private Integer promotionType;
    @ApiModelProperty("排序")
    private Integer sort;
    @Field(type =FieldType.Nested)
    @ApiModelProperty("商品其他特征")
    private List<EsProductAttributeValue> attrValueList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getRecommandStatus() {
        return recommandStatus;
    }

    public void setRecommandStatus(Integer recommandStatus) {
        this.recommandStatus = recommandStatus;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<EsProductAttributeValue> getAttrValueList() {
        return attrValueList;
    }

    public void setAttrValueList(List<EsProductAttributeValue> attrValueList) {
        this.attrValueList = attrValueList;
    }
}
