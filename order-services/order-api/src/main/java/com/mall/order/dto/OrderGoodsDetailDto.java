package com.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/20 17:34
 */
@Data
public class OrderGoodsDetailDto {
    Long goodsId;
    String goodsName;
    BigDecimal price;
    Integer num;
}
