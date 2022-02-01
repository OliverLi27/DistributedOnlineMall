package com.mall.order.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @Description
 * @Date 16:46 2020/9/19
 * @Author Xingchen Li
 */
@Data
public class GetOrderDetailResponse extends AbstractResponse {
    OrderDetailDto orderDetailDto;
}
