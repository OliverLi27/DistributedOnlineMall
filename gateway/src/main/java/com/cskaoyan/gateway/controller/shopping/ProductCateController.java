package com.cskaoyan.gateway.controller.shopping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IProductCateService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.AllProductCateRequest;
import com.mall.shopping.dto.AllProductCateResponse;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.user.annotation.Anoymous;
import com.mall.user.intercepter.TokenIntercepter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Anoymous
@RequestMapping("shopping")
@Api(tags = "ProductCateController", description = "商品种类信息")
public class ProductCateController {

    @Reference(check = false, timeout = 2000)
    IProductCateService productCateService;

    @ApiOperation("获取所有商品的种类信息")
    @ApiImplicitParam(name = "sort", value = "排序方式", dataType = "String", required = true)
    @GetMapping("categories")
    public ResponseData getAllProductCate(String sort) {
        sort = "desc";
        AllProductCateRequest allProductCateRequest = new AllProductCateRequest(sort);
        AllProductCateResponse response = productCateService.getAllProductCate(allProductCateRequest);

        if (ShoppingRetCode.SUCCESS.getMessage().equals(response.getMsg())) {
            return new ResponseUtil().setData(response.getProductCateDtoList());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }
}
