package com.mall.order.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.commons.result.ResponseUtil;
import com.mall.order.OrderQueryService;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.converter.OrderConverter;
import com.mall.order.dal.entitys.*;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dal.persistence.OrderShippingMapper;
import com.mall.order.dto.*;
import com.mall.order.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 *  Xingchen Li
 * create-date: 2019/7/30-上午10:04
 */
@Slf4j
@Component
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Autowired
    private OrderConverter orderConverter;

    @Override
    public GetOrderListDto getUserOrders(OrderListRequest request) {
        GetOrderListDto getOrderListDto = new GetOrderListDto();
        try {
            //检查请求参数
            request.requestCheck();
            //设置分页、排序
            if (request.getPage() != null && request.getSize() != null) {
                PageHelper.startPage(request.getPage(), request.getSize());
            }
            if (request.getSort() != null) {
                PageHelper.orderBy(request.getSort());
            }

            //获取当前用户下的所有订单
            Example orderExample = new Example(Order.class);
            orderExample.createCriteria().andEqualTo("userId", request.getUserId()).andNotEqualTo("status",6);
            List<Order> orders = orderMapper.selectByExample(orderExample);

            //创建返回的对象
            List<OrderDetailInfo> orderDetailInfos = new ArrayList<>();
            PageInfo<Order> orderPageInfo = new PageInfo<>(orders);

            //获取每个订单下的item和shopping
            for (Order order : orders) {
                //转换订单信息
                OrderDetailInfo orderDetailInfo = orderConverter.order2detail(order);

                //获取订单下的商品信息
                List<OrderItem> orderItems = orderItemMapper.queryByOrderId(order.getOrderId());
                log.info(orderItems.toString());

                //转换商品信息
                List<OrderItemDto> orderItemDtos = orderConverter.item2dto(orderItems);

                //获取订单下的商品物流信息
                OrderShipping orderShipping = new OrderShipping();
                orderShipping.setOrderId(order.getOrderId());
                orderShipping = orderShippingMapper.selectOne(orderShipping);

                //转换物流信息
                OrderShippingDto orderShippingDto = orderConverter.shipping2dto(orderShipping);

                //组成当前订单的参数
                orderDetailInfo.setOrderItemDto(orderItemDtos);
                orderDetailInfo.setOrderShippingDto(orderShippingDto);

                orderDetailInfos.add(orderDetailInfo);
            }
            log.info(orderDetailInfos.toString());

            //返回Json数据
            getOrderListDto.setData(orderDetailInfos);
            getOrderListDto.setTotal(orderPageInfo.getTotal());

        } catch (Exception e) {
            log.error("OrderQueryServiceImpl.getAllOrder Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(null,e);
        }
        return getOrderListDto;
    }

    @Override
    public OrderDetailDto getOrderDetail(OrderDetailRequest request) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        try {
            //检查请求参数
            request.requestCheck();
            //获得订单信息
            Order order = orderMapper.selectByPrimaryKey(request.getOrderId());
            //转换订单信息
            orderDetailDto.setUserName(order.getBuyerNick());
            orderDetailDto.setOrderTotal(order.getPayment());
            orderDetailDto.setUserId(order.getUserId());
            orderDetailDto.setOrderStatus(order.getStatus());

            //获得商品信息
            List<OrderItem> orderItems = orderItemMapper.queryByOrderId(order.getOrderId());
            //转换商品信息
            List<OrderItemDto> orderItemDtos = orderConverter.item2dto(orderItems);

            //获得物流信息
            OrderShipping orderShipping = new OrderShipping();
            orderShipping.setOrderId(order.getOrderId());
            orderShipping = orderShippingMapper.selectOne(orderShipping);
            //转换物流信息
            OrderShippingDto orderShippingDto = orderConverter.shipping2dto(orderShipping);

            //封装
            orderDetailDto.setStreetName(orderShippingDto.getReceiverAddress());
            orderDetailDto.setTel(orderShippingDto.getReceiverPhone());
            orderDetailDto.setGoodsList(orderItemDtos);

        } catch (Exception e) {
            log.error("OrderQueryServiceImpl.getOrderDetail Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(null, e);
        }
        return orderDetailDto;
    }
}
