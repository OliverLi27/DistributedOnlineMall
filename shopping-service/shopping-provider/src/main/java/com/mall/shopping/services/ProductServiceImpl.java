package com.mall.shopping.services;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.shopping.IProductService;
import com.mall.shopping.constant.GlobalConstants;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.converter.ProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.entitys.ItemDetail;
import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dal.entitys.PanelContentItem;
import com.mall.shopping.dal.persistence.ItemDetailMapper;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dal.persistence.PanelMapper;
import com.mall.shopping.dto.*;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mall.shopping.constant.GlobalConstants.*;

/**
 * @Author: Xingchen Li
 * @Date :  2020/9/16 21:22
 * @Version 1.0
 */
@Slf4j
@Component
@Service
public class ProductServiceImpl implements IProductService {

//    @Autowired
//    ProductMapper productMapper;
    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemDetailMapper itemDetailMapper;

    @Autowired
    PanelMapper panelMapper;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    ContentConverter contentConverter;
    @Override
    public ProductDetailResponse getProductDetail(ProductDetailRequest request) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        try {
            request.requestCheck();
            //RBucket<Object> bucket = redissonClient.getBucket(GlobalConstants.PRODUCT_ITEM_CACHE_KEY);
            RBucket<Object> bucket = redissonClient.getBucket(request.getId().toString());
            String str = (String) bucket.get();
            if (!StringUtil.isEmpty(str)) {
                //从redis中取值
                ProductDetailResponse response = JSON.parseObject(str, ProductDetailResponse.class);
                //修改
                response.setCode(ShoppingRetCode.SUCCESS.getCode());
                response.setMsg(ShoppingRetCode.SUCCESS.getMessage());

                return response;
            }
            ItemDetail itemDetail = itemDetailMapper.selectProductDetail(request.getId());
            ProductDetailDto productDetailDto = productConverter.detail2Dto(itemDetail);
            productDetailResponse.setProductDetailDto(productDetailDto);
            productDetailResponse.setCode(ShoppingRetCode.SUCCESS.getCode());
            productDetailResponse.setMsg(ShoppingRetCode.SUCCESS.getMessage());
            //将查询出的结果存入redis
            String jsonString = JSON.toJSONString(productDetailResponse);
            bucket.set(jsonString,PRODUCT_ITEM_EXPIRE_TIME,TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("ProductServiceImpl.getProductDetail Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(productDetailResponse,e);
        }
        return productDetailResponse;
    }

    @Override
    public AllProductResponse getAllProduct(AllProductRequest request) {
        AllProductResponse allProductResponse = new AllProductResponse();
        try {
            request.requestCheck();
            String orderCol = "price";
            String orderDir = null;
            Boolean flag = "".equals(request.getSort());
            if (flag) {
                orderDir = null;

            } else if (Integer.parseInt(request.getSort()) == -1) {
                orderDir = "desc";

            } else if (Integer.parseInt(request.getSort()) == 1) {
                orderDir = "asc";
            }

            //开启分页
            PageHelper.startPage(request.getPage(), request.getSize());
            List<Item> items = itemMapper.selectItemFront(request.getCid(),orderCol,orderDir,request.getPriceGt(),request.getPriceLte());
            List<ProductDto> productDtoList = productConverter.items2Dto(items);
            //分页
//            PageInfo<ProductDto> pageInfo = new PageInfo<>(productDtoList);
//            long total = pageInfo.getTotal();
            //List<ProductDto> list = pageInfo.getList();

            long total = itemMapper.selectProductCounts();
            allProductResponse.setTotal(total);
            allProductResponse.setProductDtoList(productDtoList);
            allProductResponse.setCode(ShoppingRetCode.SUCCESS.getCode());
            allProductResponse.setMsg(ShoppingRetCode.SUCCESS.getMessage());

        } catch (Exception e) {
            log.error("ProductServiceImpl.getAllProduct Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(allProductResponse,e);
        }
        return allProductResponse;
    }

    @Override
    public RecommendResponse getRecommendGoods() {
        RecommendResponse recommendResponse = new RecommendResponse();
        try {
            RBucket<Object> bucket = redissonClient.getBucket(GlobalConstants.RECOMMEND_PANEL_CACHE_KEY);
            String str = (String) bucket.get();
            if (!StringUtil.isEmpty(str)) {
                RecommendResponse response = JSON.parseObject(str, RecommendResponse.class);
                //修改
                response.setCode(ShoppingRetCode.SUCCESS.getCode());
                response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
                return response;
            }
            Panel panel = panelMapper.selectRecommendProduct();
            List<PanelContentItem> panelContentItemList = panelMapper.selectPanelContent(panel.getId());
            panel.setPanelContentItems(panelContentItemList);
            PanelDto panelDto = contentConverter.panen2Dto(panel);
            HashSet<PanelDto> set = new HashSet<>();
            set.add(panelDto);
            recommendResponse.setPanelContentItemDtos(set);
            recommendResponse.setCode(ShoppingRetCode.SUCCESS.getCode());
            recommendResponse.setMsg(ShoppingRetCode.SUCCESS.getMessage());

            String jsonString = JSON.toJSONString(recommendResponse);
            bucket.set(jsonString,RECOMMEND_CACHE_EXPIRE,TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("ProductServiceImpl.getRecommendGoods Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(recommendResponse,e);
        }

        return recommendResponse;
    }
}
