package com.mall.order;

import com.mall.order.dto.*;

import java.util.List;

/**
 *  Xingchen Li
 * create-date: 2019/7/30-上午9:13
 * 订单相关业务
 */
public interface OrderCoreService {

    /**
     * 创建订单
     * @param request
     * @return
     */
    CreateOrderResponse createOrder(CreateOrderRequest request);

    /**
     * 取消订单
     * @param request
     * @return
     */
    Boolean cancelOrder(CancelOrderRequest request);

    /**
     * 删除订单
     * @param request
     * @return
     */
    Boolean deleteOrder(DeleteOrderRequest request);

    /**
     * 创建秒杀订单
     * @param request
     * @return
     */
    CreateSeckillOrderResponse createSeckillOrder(CreateSeckillOrderRequest request);


    /**
     * 订单统计
     * @param request
     * @return
     */
    OrderCountResponse orderCount(OrderCountRequest request);


    //查询订单的商品(供pay接口使用)
    List<OrderItemDto> orderGoodsDetail(String orderId);

    //查询订单
    OrderDto selectOrderByOrderId(String orderId);


}
