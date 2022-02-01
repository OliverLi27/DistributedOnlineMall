package com.cskaoyan.gateway.controller.shopping;


import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IContentService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.shopping.dto.NavListResponse;
import com.mall.user.annotation.Anoymous;
import com.mall.user.intercepter.TokenIntercepter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("shopping")
@Api(tags = "ContentController", description = "导航栏显示")
@Anoymous
public class ContentController {

    @Reference(check = false)
    IContentService contentService;

    @ApiOperation("获取导航栏的信息")
    @GetMapping("navigation")
    public ResponseData categories() {
        NavListResponse response = contentService.queryNavList();

        if (ShoppingRetCode.SUCCESS.getMessage().equals(response.getMsg())) {
            return new ResponseUtil().setData(response.getPannelContentDtos());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }
}
