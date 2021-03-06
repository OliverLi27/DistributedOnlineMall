package com.cskaoyan.gateway.controller.shopping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.gateway.form.shopping.AddressForm;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.user.IAddressService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.*;
import com.mall.user.intercepter.TokenIntercepter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * create by Xingchen Li on 2020/03/05
 */
@Slf4j
@RestController
@RequestMapping("/shopping")
@Api(tags = "AddressController", description = "地址控制层")
public class AddressController {

    @Reference(timeout = 3000,check = false)
    IAddressService addressService;

    /**
     * 获取当前用户的地址列表
     *
     * @return
     */
    @GetMapping("/addresses")
    @ApiOperation("获取当前用户的地址列表")
    public ResponseData addressList(HttpServletRequest request) {
        //String userInfo = (String) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        Long uid = (Long) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        //JSONObject object = JSON.parseObject(userInfo);
        //Long uid = Long.parseLong(object.get("uid").toString());
        AddressListRequest addressListRequest = new AddressListRequest();
        addressListRequest.setUserId(uid);
        AddressListResponse response = addressService.addressList(addressListRequest);
        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getAddressDtos());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }

    @ApiOperation("添加地址")
    @PostMapping("/addresses")
    @ApiImplicitParam(name = "form", value = "地址信息", dataType = "AddressForm", required = true)
    public ResponseData addressAdd(@RequestBody AddressForm form, HttpServletRequest servletRequest) {
        log.debug(form.is_Default()+"");
        log.debug(form.toString());

        AddAddressRequest request = new AddAddressRequest();
        Long uid = (Long) servletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        request.setUserId(uid);
        request.setUserName(form.getUserName());
        request.setStreetName(form.getStreetName());
        request.setTel(form.getTel());
        request.setIsDefault(form.is_Default() ? 1 : null);
        AddAddressResponse response = addressService.createAddress(request);

        if (response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getMsg());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }

    @ApiOperation("删除地址")
    @DeleteMapping("/addresses/{addressid}")
    @ApiImplicitParam(name = "addressid", value = "地址ID", paramType = "path", required = true)
    public ResponseData addressDel(@PathVariable("addressid") Long addressid) {
        DeleteAddressRequest request = new DeleteAddressRequest();
        request.setAddressId(addressid);
        DeleteAddressResponse response = addressService.deleteAddress(request);

        if (response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getMsg());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());

    }

    @ApiOperation("更新地址")
    @PutMapping("/addresses")
    @ApiImplicitParam(name = "form", value = "地址信息", dataType = "AddressForm", required = true)
    public ResponseData addressUpdate(@RequestBody AddressForm form, HttpServletRequest servletRequest) {
        UpdateAddressRequest request = new UpdateAddressRequest();
        Long uid = (Long) servletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        request.setAddressId(form.getAddressId());
        request.setIsDefault(form.is_Default() ? 1 : null);
        request.setStreetName(form.getStreetName());
        request.setTel(form.getTel());
        request.setUserId(uid);
        request.setUserName(form.getUserName());

        UpdateAddressResponse response = addressService.updateAddress(request);

        if (response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getMsg());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }
}
