package com.mall.order.biz.handler;

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.constant.OrderRetCode;
import com.mall.shopping.ICartService;
import com.mall.shopping.dto.ClearCartItemRequest;
import com.mall.shopping.dto.ClearCartItemResponse;
import com.mall.shopping.dto.DeleteCheckedItemRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  Xingchen Li
 * create-date: 2019/8/1-下午5:05
 * 将购物车中的缓存失效
 */
@Slf4j
@Component
public class ClearCartItemHandler extends AbstractTransHandler {

    @Reference(check = false)
    ICartService iCartService;

    //是否采用异步方式执行
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean handle(TransHandlerContext context) {
        log.warn("ClearCartItemHandler");
        CreateOrderContext createOrderContext = (CreateOrderContext) context;
        //将购物车中的商品缓存删掉
        DeleteCheckedItemRequest deleteCheckedItemRequest = new DeleteCheckedItemRequest();
        deleteCheckedItemRequest.setUserId(createOrderContext.getUserId());
        iCartService.deleteCheckedItem(deleteCheckedItemRequest);
        return true;
    }
}
