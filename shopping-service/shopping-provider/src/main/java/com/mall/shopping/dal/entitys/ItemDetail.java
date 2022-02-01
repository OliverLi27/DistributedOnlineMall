package com.mall.shopping.dal.entitys;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Xingchen Li
 * @Date :  2020/9/17 11:03
 * @Version 1.0
 */

public class ItemDetail {

    @Id
    private Integer id;//productId

    private Integer limitNum;//limitNum

    private String title;//productName

    private BigDecimal price;//salePrice

    @Transient
    private String productImageBig;//image的第一个

    private String image;//productImageSmall

    private List<String> images;

    private String detail;//item_desc表中的item_desc

    private String sellPoint;//subTitle

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public void setProductImageBig(String productImageBig) {
        this.productImageBig = productImageBig;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String[] getImages() {
        if (image != null && !"".equals(image)) {
            String[] strings = image.split(",");
            return strings;
        }
        return null;
    }

    public String getProductImageBig(){
        if (image != null && !"".equals(image)) {
            String[] strings = image.split(",");
            return strings[0];
        }
        return null;
    }

    @Override
    public String toString() {
        return "ItemDetail{" +
                "id=" + id +
                ", limitNum=" + limitNum +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", productImageBig='" + productImageBig + '\'' +
                ", image=" + image +
                ", detail='" + detail + '\'' +
                ", sellPoint='" + sellPoint + '\'' +
                '}';
    }
}
