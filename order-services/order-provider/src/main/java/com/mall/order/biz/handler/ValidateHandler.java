package com.mall.order.biz.handler;

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.user.IAddressService;
import com.mall.user.IMemberService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  Xingchen Li
 * create-date: 2019/8/1-下午4:47
 *
 */
@Slf4j
@Component
public class ValidateHandler extends AbstractTransHandler {

    @Reference(check = false)
    private IMemberService memberService;
    @Reference(check = false)
    private IAddressService iAddressService;

    /**
     * 验证用户合法性
     * @return
     */

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean handle(TransHandlerContext context) {
        log.warn("ValidateHandler");
        //将context对象转型
        CreateOrderContext createOrderContext = (CreateOrderContext) context;

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setAddressId(createOrderContext.getAddressId());
        addressRequest.setUserId(createOrderContext.getUserId());

        //根据用户id查询用户信息（收货人姓名)
        AddressResponse response = iAddressService.addressUserName(addressRequest);

        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            String username = response.getUserName();
            if (!username.equals(createOrderContext.getUserName())) {
                //数据库中的用户信息和传入的不匹配
                throw new BizException(response.getCode(), response.getMsg());
            }

            createOrderContext.setBuyerNickName(username);
        }
        return true;
    }
}
