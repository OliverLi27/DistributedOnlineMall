package com.cskaoyan.gateway.controller.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.order.OrderCoreService;
import com.mall.order.OrderQueryService;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dto.*;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.intercepter.TokenIntercepter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 用户订单层
 * @Date 22:28 2020/9/17
 * @Author Xingchen Li
 */
@RestController
@RequestMapping("/shopping")
@Api(tags = "OrderController")
public class OrderController {

    @Reference(check = false)
    private OrderCoreService orderCoreService;

    @Reference(check = false)
    private OrderQueryService orderQueryService;

    /**
     * 创建订单
     */
    @PostMapping("/order")
    @ApiOperation("创建订单")
    public ResponseData createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest servletRequest) {
        //获取用户信息
        Long uid = (Long) servletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        request.setUserId(uid);
        //设置 uniqueKey
        request.setUniqueKey(UUID.randomUUID().toString());

        //创建订单
        CreateOrderResponse response = orderCoreService.createOrder(request);
        if (response.getCode().equals(OrderRetCode.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData(response.getOrderId());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @GetMapping("/order")
    public ResponseData getAllOrders(Integer page, Integer size, HttpServletRequest servletRequest) {
        //获取用户信息
        Long uid = (Long) servletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);

        OrderListRequest orderListRequest = new OrderListRequest();
        orderListRequest.setUserId(uid);
        orderListRequest.setPage(page);
        orderListRequest.setSize(size);
        orderListRequest.setSort("create_time desc");
        GetOrderListDto response = orderQueryService.getUserOrders(orderListRequest);

        if (response != null) {
            ResponseData responseData = new ResponseUtil().setData(response);
            return responseData;
        }
        return new ResponseUtil().setErrorMsg("exception");
    }

    @GetMapping("order/{id}")
    public ResponseData getUserOrderDetail(@PathVariable(name = "id") String id) {
        OrderDetailRequest request = new OrderDetailRequest();
        //订单id
        request.setOrderId(id);

        OrderDetailDto response = orderQueryService.getOrderDetail(request);

        if (response != null) {
            ResponseData responseData = new ResponseUtil().setData(response);
            return responseData;
        }
        return new ResponseUtil().setErrorMsg("Exception");

    }

    @PostMapping("cancelOrder")
    public ResponseData cancelOrder(@RequestBody Map map) {
        String orderId = (String) map.get("orderId");

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setOrderId(orderId);
        Boolean response = orderCoreService.cancelOrder(cancelOrderRequest);

        if (response == true) {
            ResponseData responseData = new ResponseUtil().setData(response);
            return responseData;
        }
        return new ResponseUtil().setErrorMsg("exception");
    }

    @DeleteMapping("/order/{id}")
    public ResponseData deleteOrder(@PathVariable(name = "id") String id) {
        DeleteOrderRequest deleteOrderRequest = new DeleteOrderRequest();
        deleteOrderRequest.setOrderId(id);

        Boolean response = orderCoreService.deleteOrder(deleteOrderRequest);

        if (response == true) {
            ResponseData responseData = new ResponseUtil().setData(response);
            return responseData;
        }
        return new ResponseUtil().setErrorMsg("exception");
    }
}
