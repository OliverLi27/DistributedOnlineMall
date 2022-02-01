package com.mall.order.services;

import com.mall.order.OrderCoreService;
import com.mall.order.biz.TransOutboundInvoker;
import com.mall.order.biz.context.AbsTransHandlerContext;
import com.mall.order.biz.factory.OrderProcessPipelineFactory;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.constants.OrderConstants;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.entitys.OrderShipping;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dal.persistence.OrderShippingMapper;
import com.mall.order.dal.persistence.StockMapper;
import com.mall.order.dto.*;
import com.mall.order.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *  Xingchen Li
 * create-date: 2019/7/30-上午10:05
 */
@Slf4j
@Component
@Service(cluster = "failfast")
public class OrderCoreServiceImpl implements OrderCoreService {

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	OrderItemMapper orderItemMapper;

	@Autowired
	OrderShippingMapper orderShippingMapper;

	@Autowired
	StockMapper stockMapper;

	@Autowired
    OrderProcessPipelineFactory orderProcessPipelineFactory;


	/**
	 * 创建订单的处理流程
	 * @param request
	 * @return
	 */
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		CreateOrderResponse response = new CreateOrderResponse();
		try {
			//创建pipeline对象
			TransOutboundInvoker invoker = orderProcessPipelineFactory.build(request);

			//启动pipeline
			invoker.start(); //启动流程（pipeline来处理）

			//获取处理结果
			AbsTransHandlerContext context = invoker.getContext();

			//把处理结果转换为response
			response = (CreateOrderResponse) context.getConvert().convertCtx2Respond(context);
		} catch (Exception e) {
			//异常处理机制
			log.error("OrderCoreServiceImpl.createOrder Occur Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

	@Override
	public Boolean cancelOrder(CancelOrderRequest request) {
		CancelOrderResponse response = new CancelOrderResponse();
		try {
			request.requestCheck();
			String orderId = request.getOrderId();

			Order order = new Order();
			order.setStatus(7);
			//修改订单表
			orderMapper.updateOrderStatus(5, orderId);

			//修改订单商品关联表
			orderItemMapper.updateStockStatus(2, orderId);

			//获得取消的订单中的商品id,去stock表里增加库存
			Example orderItemExample = new Example(OrderItem.class);
			orderItemExample.createCriteria().andEqualTo("orderId", orderId);
			List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
			for (OrderItem orderItem : orderItems) {
				Long itemId = orderItem.getItemId();
				Integer num = orderItem.getNum();
				//查找商品当前库存
				Stock stock = stockMapper.selectStock(itemId);
				//增加库存，减少锁定库存
				stock.setStockCount(Long.valueOf(num));
				stock.setLockCount(-num);
				//更新库存
				stockMapper.updateStock(stock);
			}

		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.cancelOrder Occur Exception :" + e);
			response.setCode(OrderRetCode.DB_EXCEPTION.getCode());
			response.setMsg(OrderRetCode.DB_EXCEPTION.getMessage());
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return true;
	}

	@Override
	public CreateSeckillOrderResponse createSeckillOrder(CreateSeckillOrderRequest request) {
		return null;
	}

	@Override
	public Boolean deleteOrder(DeleteOrderRequest request) {
		DeleteOrderResponse response = new DeleteOrderResponse();
		try {
			request.requestCheck();
			String orderId = request.getOrderId();

			//判断是否要改库存表
			Order order = orderMapper.selectByPrimaryKey(orderId);
			if (order.getStatus() == 0) {
				//获得取消的订单中的商品id,去stock表里增加库存
				Example orderItemDeleteExample = new Example(OrderItem.class);
				orderItemDeleteExample.createCriteria().andEqualTo("orderId", orderId);
				List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemDeleteExample);
				for (OrderItem orderItem : orderItems) {
					Long itemId = orderItem.getItemId();
					Integer num = orderItem.getNum();
					//查找商品当前库存
					Stock stock = stockMapper.selectStock(itemId);
					//增加库存，减少锁定库存
					stock.setStockCount(Long.valueOf(num));
					stock.setLockCount(-num);
					//更新库存
					stockMapper.updateStock(stock);
				}
			}

			//订单表修改订单状态
			Order order1 = new Order();
			order1.setOrderId(orderId);
			order1.setStatus(6);
			orderMapper.updateByPrimaryKeySelective(order1);
			//修改订单商品表状态
			OrderItem orderItem = new OrderItem();
			orderItem.setStatus(2);
			Example orderItemExample = new Example(OrderItem.class);
			orderItemExample.createCriteria().andEqualTo("orderId", orderId);
			orderItemMapper.updateByExampleSelective(orderItem,orderItemExample);

		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.deleteOrder occur Exception：" + e);

			response.setCode(OrderRetCode.DB_EXCEPTION.getCode());
			response.setMsg(OrderRetCode.DB_EXCEPTION.getMessage());
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return true;
	}

	@Override
	public OrderCountResponse orderCount(OrderCountRequest request) {
		return null;
	}
	@Override
	public List<OrderItemDto> orderGoodsDetail(String orderId) {
		ArrayList<OrderItemDto> orderItemDtos = new ArrayList<>();
		Example example = new Example(OrderItem.class);
		example.createCriteria().andEqualTo("orderId", orderId);
		List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
		for (OrderItem orderItem : orderItems) {
			OrderItemDto orderItemDto = new OrderItemDto();
			orderItemDto.setItemId(orderItem.getItemId().toString());
			orderItemDto.setTitle(orderItem.getTitle());
			orderItemDto.setNum(orderItem.getNum());
			orderItemDto.setPrice(BigDecimal.valueOf(orderItem.getPrice()));
			orderItemDtos.add(orderItemDto);
		}
		return orderItemDtos;
	}

	@Override
	public OrderDto selectOrderByOrderId(String orderId) {
		OrderDto orderDto = new OrderDto();
		Order order = orderMapper.selectByPrimaryKey(orderId);
		orderDto.setOrderId(orderId);
		orderDto.setPayment(order.getPayment());
		return orderDto;
	}

//	@Override
//	public List<OrderItemDto> orderGoodsDetail(String orderId) {
//		ArrayList<OrderItemDto> orderItemDtos = new ArrayList<>();
//		Example example = new Example(OrderItem.class);
//		example.createCriteria().andEqualTo("orderId", orderId);
//		List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
//		for (OrderItem orderItem : orderItems) {
//			OrderItemDto orderItemDto = new OrderItemDto();
//			orderItemDto.setItemId(orderItem.getItemId().toString());
//			orderItemDto.setTitle(orderItem.getTitle());
//			orderItemDto.setNum(orderItem.getNum());
//			orderItemDto.setPrice(BigDecimal.valueOf(orderItem.getPrice()));
//			orderItemDtos.add(orderItemDto);
//		}
//		return orderItemDtos;
//	}

}
