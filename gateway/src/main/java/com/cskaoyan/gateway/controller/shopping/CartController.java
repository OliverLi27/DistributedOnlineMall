package com.cskaoyan.gateway.controller.shopping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.ICartService;
import com.mall.shopping.dto.AddCartRequest;
import com.mall.shopping.dto.AddCartResponse;
import com.mall.shopping.dto.CartListByIdRequest;
import com.mall.shopping.dto.CartListByIdResponse;
import com.mall.user.intercepter.TokenIntercepter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.*;
import com.mall.user.annotation.Anoymous;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;



import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("shopping")
@Api(tags = "CartController", description = "购物车控制模块")
public class CartController {

    @Reference(check = false, timeout = 2000, retries = 0)
    ICartService cartService;

    @ApiOperation("获取用户购物车列表")
    @GetMapping("carts")
    public ResponseData carts(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        CartListByIdResponse response = cartService.getCartListById(new CartListByIdRequest(uid));
        if (ShoppingRetCode.SUCCESS.getMessage().equals(response.getMsg())) {
            return new ResponseUtil().setData(response.getCartProductDtos());
        }
        return new ResponseUtil().setErrorMsg("fail");
    }



    @ApiOperation("添加购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "productNum", value = "商品数量", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "Long", required = true)
    })
    @PostMapping("carts")
    public ResponseData addCart(@RequestBody AddCartDto addCartDto, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        AddCartResponse addCartResponse = cartService.
                addToCart(new AddCartRequest(uid, addCartDto.getProductId(), addCartDto.getProductNum()));
        if (ShoppingRetCode.SUCCESS.getMessage().equals(addCartResponse.getMsg())) {
            return new ResponseUtil().setData(null);
        }
        return new ResponseUtil().setErrorMsg(addCartResponse.getMsg());
    }


    /**
     * 更新 lzc
     *
     * @param request
     * @return
     */
    @PutMapping("carts")
    public ResponseData updateCart(@RequestBody UpdateCartNumRequest request) {

        UpdateCartNumResponse response = cartService.updateCartNum(request);

        if (response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData("成功");
        }
        return new ResponseUtil<>().setErrorMsg("fail");
    }

    /**
     * 删除 lzc
     *
     * @param uid
     * @param pid
     * @return
     */
    @DeleteMapping("carts/{uid}/{pid}")
    public ResponseData deleteCartByTwo(@PathVariable("uid") Long uid,
                                        @PathVariable("pid") Long pid) {
        DeleteCartItemRequest deleteCartItemRequest = new DeleteCartItemRequest();
        deleteCartItemRequest.setUserId(uid);
        deleteCartItemRequest.setItemId(pid);

        DeleteCartItemResponse response = cartService.deleteCartItem(deleteCartItemRequest);

        if (response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData("成功");
        }
        return new ResponseUtil<>().setErrorMsg("fail");

    }

    /**
     * 删除 lzc
     * @param userId
     * @return
     */

    @DeleteMapping("items/{id}")
    public ResponseData deleteCart(@PathVariable("id") Long userId) {
        DeleteCheckedItemRequest deleteCheckedItemRequest = new DeleteCheckedItemRequest();
        deleteCheckedItemRequest.setUserId(userId);

        DeleteCheckedItemResposne resposne = cartService.deleteCheckedItem(deleteCheckedItemRequest);

        if (resposne.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData("成功");
        }
        return new ResponseUtil<>().setErrorMsg("fail");


    }


}
