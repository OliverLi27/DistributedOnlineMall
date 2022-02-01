package com.mall.pay;

import com.mall.pay.dto.PayQueryStatusResponse;
import com.mall.pay.dto.PayRequest;
import com.mall.pay.dto.PayResponse;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/19 15:21
 */

public interface IPayService {

    PayResponse createPicToPay(PayRequest payRequest,String add);

    PayQueryStatusResponse queryStatus(String orderId);

}
