package com.cskaoyan.gateway.controller.shopping;


import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IProductService;
import com.mall.shopping.dto.*;
import com.mall.user.annotation.Anoymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Xingchen Li
 * @Date :  2020/9/16 20:20
 * @Version 1.0
 */
@RestController
@RequestMapping("/shopping")
@Api(tags = "PagingQueryProductsController",description = "分页查询商品列表")
@Anoymous
public class ProductsController {
    @Reference(timeout = 3000,check = false)
    IProductService productService;

    @GetMapping("/goods")
    @ApiOperation("获取所有商品并分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", paramType = "query"),
        @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query"),
        @ApiImplicitParam(name = "sort", value = "是否排序", paramType = "query"),
        @ApiImplicitParam(name = "priceGt", value = "价格最小值", paramType = "query"),
        @ApiImplicitParam(name = "priceLte", value = "价格最大值", paramType = "query"),
        @ApiImplicitParam(name = "cid", value = "页码", paramType = "query"),
    })
    public ResponseData queryAllProducts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "priceGt", required = false) Integer priceGt,
            @RequestParam(value = "priceLte", required = false) Integer priceLte,
            @RequestParam(value = "cid", required = false) Long cid
    ) {

        AllProductRequest request = new AllProductRequest();
        request.setPage(page);
        request.setSize(size);
        request.setSort(sort);
        request.setPriceGt(priceGt);
        request.setPriceLte(priceLte);
        request.setCid(cid);

        AllProductResponse response = productService.getAllProduct(request);
        //修改
        AllProductDto allProductDto = new AllProductDto();
        allProductDto.setData(response.getProductDtoList());
        allProductDto.setTotal(response.getTotal());

        ResponseData responseData = new ResponseUtil().setData(allProductDto);
        return responseData;
    }

    @GetMapping("/product/{id}")
    @ApiOperation("查看商品详情")
    @ApiImplicitParam(name = "id", value = "商品id", paramType = "path", required = true)
    public ResponseData queryProductDetail(@PathVariable("id") Long id) {
        ProductDetailRequest request = new ProductDetailRequest();
        request.setId(id);
        ProductDetailResponse response = productService.getProductDetail(request);
        ResponseData productDetailResponseResponseData =
                new ResponseUtil().setData(response.getProductDetailDto());
        return productDetailResponseResponseData;
    }

    @GetMapping("/recommend")
    @ApiOperation("查询推荐商品")
    public ResponseData queryRecommendProduct() {
        RecommendResponse response = productService.getRecommendGoods();
        return new ResponseUtil().setData(response.getPanelContentItemDtos());
    }
}
