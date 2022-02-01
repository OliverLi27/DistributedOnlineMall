package com.mall.pay.dto;

import com.mall.commons.result.AbstractRequest;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/19 16:57
 */
@Data
public class PayRequest extends AbstractRequest {

    private String nickName;
    private BigDecimal money;
    private String info;
    private String orderId;
    private String payType;

    @Override
    public void requestCheck() {

    }
}
