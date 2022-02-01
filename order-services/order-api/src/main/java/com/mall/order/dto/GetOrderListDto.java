package com.mall.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Date 19:07 2020/9/19
 * @Author Xingchen Li
 */
@Data
public class GetOrderListDto implements Serializable {
    private List<OrderDetailInfo> data;
    private Long total;
}
