package com.mall.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Date 17:06 2020/9/18
 * @Author Xingchen Li
 */
@Data
public class OrderDetailDto implements Serializable {
    private String userName;
    private BigDecimal orderTotal;
    private Long userId;
    private List<OrderItemDto> goodsList;
    private String tel;
    private String streetName;
    private Integer orderStatus;

}
