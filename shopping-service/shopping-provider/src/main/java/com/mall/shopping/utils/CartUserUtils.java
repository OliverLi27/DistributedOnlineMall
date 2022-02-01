package com.mall.shopping.utils;

import org.springframework.beans.factory.annotation.Autowired;

public class CartUserUtils {

    public static String cartUserId(Long userId) {
        return new StringBuilder("cart_user_id_").append(userId).toString();
    }

    public static String cartItemId(Long itemId) {
        return new StringBuilder().append("product_id").append(itemId).toString();
    }

//    public static void main(String[] args) {
//        long userId = 123;
//        String userid = CartUserIdUtils.cartUserId(userId);
//        System.out.println(userid);
//    }
}
