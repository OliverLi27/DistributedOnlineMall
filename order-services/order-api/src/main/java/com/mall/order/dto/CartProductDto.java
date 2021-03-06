package com.mall.order.dto;/**
 * Created by Xingchen Li on 2019/8/1.
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  Xingchen Li
 * create-date: 2019/8/1-下午9:33
 */
@Data
public class CartProductDto implements Serializable{

    private Long productId;

    private BigDecimal salePrice;

    private Long productNum;

    private Long limitNum;

    private String checked;

    private String productName;

    private String productImg;
}
