package com.mall.order;

import com.mall.order.dto.*;

/**
 *  Xingchen Li
 * create-date: 2019/7/30-上午10:01
 */
public interface OrderQueryService {

    /**
     * 查询当前user下的订单列表
     * @param request
     * @return
     */
    GetOrderListDto getUserOrders(OrderListRequest request);

    /**
     * 获取订单详细页
     * @param request
     * @return
     */
    OrderDetailDto getOrderDetail(OrderDetailRequest request);
}
