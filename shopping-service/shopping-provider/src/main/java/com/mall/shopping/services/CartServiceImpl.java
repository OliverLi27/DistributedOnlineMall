package com.mall.shopping.services;

import com.alibaba.fastjson.JSON;
import com.mall.shopping.ICartService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.CartProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dto.*;
import com.mall.shopping.utils.CartUserUtils;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@Component
public class CartServiceImpl implements ICartService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    CartProductConverter cartProductConverter;

    @Override
    public CartListByIdResponse getCartListById(CartListByIdRequest request) {
        log.warn("init CartServiceImpl.getCartListById request: {}",request);
        CartListByIdResponse response = new CartListByIdResponse();
        try {
            request.requestCheck();
            RMap<Object, Object> map = redissonClient.getMap(CartUserUtils.cartUserId(request.getUserId()));
            Set<Map.Entry<Object, Object>> entries = map.readAllEntrySet();
//            List<CartProductDto> list = cartProductConverter.itemList2Dto(entries);
            List list = new ArrayList();
            for (Map.Entry<Object, Object> entry : entries) {
                String value = (String) entry.getValue();
                CartProductDto cartProductDto = JSON.parseObject(value, CartProductDto.class);
                list.add(cartProductDto);
            }
            response.setCartProductDtos(list);
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("CartServiceImpl.getCartListById occur Exception: {}", e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public AddCartResponse addToCart(AddCartRequest request) {
        log.error("init CartServiceImpl.addToCart request: {}", request);
        AddCartResponse addCartResponse = new AddCartResponse();

        try {
            //校验数据
            request.requestCheck();
            RMap<Object, Object> map = redissonClient.getMap(CartUserUtils.cartUserId(request.getUserId()));
            String jsonStr = (String) map.get(CartUserUtils.cartItemId(request.getItemId()));
            boolean exists = jsonStr != null;
            CartProductDto cartProductDto;

            if (exists) {
                cartProductDto = JSON.parseObject(jsonStr, CartProductDto.class);
                cartProductDto.setProductNum(cartProductDto.getProductNum() + request.getNum());
            } else {
                Item item = itemMapper.selectByPrimaryKey(request.getItemId().toString());
                String image = item.getImages()[0];
                item.setImage(image);
                cartProductDto = cartProductConverter.item2Dto(item);
                cartProductDto.setProductNum(request.getNum().longValue());
                cartProductDto.setChecked("true");
            }

            map.put(CartUserUtils.cartItemId(request.getItemId()), JSON.toJSONString(cartProductDto));

            addCartResponse.setCode(ShoppingRetCode.SUCCESS.getCode());
            addCartResponse.setMsg(ShoppingRetCode.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("CartServiceImpl.addToCart occur Exception: {}", e);
            ExceptionProcessorUtils.wrapperHandlerException(addCartResponse, e);
        }
        return addCartResponse;
    }

    /**
     * 更新购物车
     *
     * @param request
     * @return
     */
    @Override
    public UpdateCartNumResponse updateCartNum(UpdateCartNumRequest request) {

        log.info("entry updateCartNum" + request.getUserId(), request);
        UpdateCartNumResponse response = new UpdateCartNumResponse();

        try {
            request.requestCheck();

            RMap<Object, Object> map = redissonClient.getMap(CartUserUtils.cartUserId(request.getUserId()));
            String jsonS = (String) map.get(CartUserUtils.cartItemId(request.getProductId()));
            CartProductDto cartProductDto = JSON.parseObject(jsonS, CartProductDto.class);
            cartProductDto.setProductNum(request.getProductNum().longValue());
            cartProductDto.setChecked(request.getChecked());

            String jsonString = JSON.toJSONString(cartProductDto);
            map.put(CartUserUtils.cartItemId(request.getProductId()), jsonString);

            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());

        } catch (Exception e) {
            log.info("CartServiceImpl.updateCartNum occur Exception: {} ","" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);

        }
        return response;


    }

    @Override
    public CheckAllItemResponse checkAllCartItem(CheckAllItemRequest request) {
        return null;
    }

    /**
     * 删除
     * @param request
     * @return
     */
    @Override
    public DeleteCartItemResponse deleteCartItem(DeleteCartItemRequest request) {
        DeleteCartItemResponse response = new DeleteCartItemResponse();
        log.info("entry deletecartItem : {}", request);

       try{
           request.requestCheck();
           RMap<Object, Object> map = redissonClient.getMap(CartUserUtils.cartUserId(request.getUserId()));
           long l = map.fastRemove(CartUserUtils.cartItemId(request.getItemId()));

           if (l == 1l) {
               response.setCode(ShoppingRetCode.SUCCESS.getCode());
               response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
           }
       }catch (Exception e){
           log.info("cartServiceImpl.deleteCartItem occur Exception",e);
           ExceptionProcessorUtils.wrapperHandlerException(response, e);

       }
        return response;
    }


    /**
     *
     *根据checked来删除
     * @param request
     * @return
     */

    @Override
    public DeleteCheckedItemResposne deleteCheckedItem(DeleteCheckedItemRequest request) {

        log.info("entry cartServiceImpl.deltecheckedItem",request);
        DeleteCheckedItemResposne resposne = new DeleteCheckedItemResposne();

        try{
           request.requestCheck();
           RMap<Object, Object> map = redissonClient.getMap(CartUserUtils.cartUserId(request.getUserId()));
           Set<Object> keySet = map.readAllKeySet();
           for (Object key : keySet) {
               String jsonStr = (String) map.get(key);
               CartProductDto cartProductDto = JSON.parseObject(jsonStr, CartProductDto.class);
               if (cartProductDto!=null){
                   if ("true".equals(cartProductDto.getChecked())){
                       map.fastRemove(CartUserUtils.cartItemId(cartProductDto.getProductId()));
                   }
               }

           }
           resposne.setCode(ShoppingRetCode.SUCCESS.getCode());
           resposne.setMsg(ShoppingRetCode.SUCCESS.getMessage());
       }catch(Exception e){
           log.info("cartServiceImpl.deletecheckedItem",e);
           ExceptionProcessorUtils.wrapperHandlerException(resposne, e);

        }
       return resposne;

    }

    @Override
    public ClearCartItemResponse clearCartItemByUserID(ClearCartItemRequest request) {
        return null;
    }



}
