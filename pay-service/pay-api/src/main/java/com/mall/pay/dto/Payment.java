package com.mall.pay.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/21 17:50
 */
@Data
public class Payment {
    Integer id;
    String orderId;
    String status;
    BigDecimal orderAmount;
    String payWay;
    Date paySuccessTime;
    Date completeTime;
}
