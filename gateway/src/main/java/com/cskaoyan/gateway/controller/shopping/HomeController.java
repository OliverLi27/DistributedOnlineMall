package com.cskaoyan.gateway.controller.shopping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IHomeService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.user.annotation.Anoymous;
import com.mall.user.intercepter.TokenIntercepter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("shopping")
@Api(tags = "Homecontroller", description = "主页信息")
@Anoymous
public class HomeController {

    @Reference(check = false)
    IHomeService homeService;

    @ApiOperation("获取主页的商品信息")
    @RequestMapping("homepage")
    public ResponseData homePage() {

        HomePageResponse response = homeService.homepage();
        if (ShoppingRetCode.SUCCESS.getMessage().equals(response.getMsg())) {
            return new ResponseUtil().setData(response.getPanelContentItemDtos());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }
}
