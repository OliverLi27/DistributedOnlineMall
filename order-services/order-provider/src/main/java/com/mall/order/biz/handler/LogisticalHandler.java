package com.mall.order.biz.handler;/**
 * Created by ciggar on 2019/8/1.
 */

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dal.entitys.OrderShipping;
import com.mall.order.dal.persistence.OrderShippingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *  Xingchen Li
 * create-date: 2019/8/1-下午5:06
 *
 * 处理物流信息（商品寄送的地址）
 */
@Slf4j
@Component
public class LogisticalHandler extends AbstractTransHandler {

    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean handle(TransHandlerContext context) {
        log.warn("LogisticalHandler");
        CreateOrderContext createOrderContext = (CreateOrderContext) context;

        OrderShipping orderShipping = new OrderShipping();
        orderShipping.setOrderId(createOrderContext.getOrderId());
        orderShipping.setReceiverName(createOrderContext.getUserName());
        orderShipping.setReceiverAddress(createOrderContext.getStreetName());
        orderShipping.setReceiverPhone(createOrderContext.getTel());
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());

        //需要处理插入失败的异常
        int effectedRows = orderShippingMapper.insert(orderShipping);
        if (effectedRows != 1) {
            throw new BizException(OrderRetCode.SHIPPING_DB_SAVED_FAILED.getCode(), OrderRetCode.SHIPPING_DB_SAVED_FAILED.getMessage());
        }

        return true;
    }
}
