package com.mall.shopping.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.shopping.ICartService;
import com.mall.shopping.dto.*;
import com.mall.shopping.services.CartServiceImpl;
import com.mall.shopping.utils.CartUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class CartServiceTest extends ShoppingProviderApplicationTests {

    @Autowired
    private ICartService cartService;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void addCart() throws IOException {
        AddCartRequest addCartRequest = new AddCartRequest();
        long itemId = 100046401;
        long userId = 2500;
//        addCartRequest.setUserId((long) 2500);
//        addCartRequest.setItemId((long) 100023501);
        addCartRequest.setUserId(userId);
        addCartRequest.setItemId(itemId);

        addCartRequest.setNum(2);
        AddCartResponse addCartResponse = cartService.addToCart(addCartRequest);
        log.error("CartService.addCart response: {}", addCartResponse);

        RMap<Object, Object> map = redissonClient.getMap(CartUserUtils.cartUserId(userId));
        //存入的类型要和取出的类型一致
        String json = (String) map.get(CartUserUtils.cartItemId(itemId));
        log.warn("json: " + json);
        CartProductDto cartProductDto = JSON.parseObject(json, CartProductDto.class);
        log.warn("cartProductDto: " + cartProductDto);
        System.in.read();
    }


    @Test
    public void cartList() throws IOException {
        long itemId = 100046401;
        long userId = 2500;
        CartListByIdRequest request = new CartListByIdRequest();
        request.setUserId(userId);

        CartListByIdResponse cartListById = cartService.getCartListById(request);
        log.warn("cartListById:{}", cartListById);
        System.in.read();
    }
}
