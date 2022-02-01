package com.mall.order.dto;/**
 * Created by Xingchen Li on 2019/7/30.
 */

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 *  Xingchen Li
 * create-date: 2019/7/30-上午9:49
 */
@Data
public class CreateOrderResponse extends AbstractResponse{

    private String orderId;
}
