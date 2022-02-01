package com.mall.order.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.order.dal.entitys.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper extends TkMapper<Order> {
    Long countAll();
    void updateOrderStatus(@Param("status") Integer status, @Param("orderId") String orderId);
}