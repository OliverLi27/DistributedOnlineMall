package com.mall.shopping.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall.shopping.IProductCateService;
import com.mall.shopping.constant.GlobalConstants;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ProductCateConverter;
import com.mall.shopping.dal.entitys.ItemCat;
import com.mall.shopping.dal.persistence.ItemCatMapper;
import com.mall.shopping.dto.AllProductCateRequest;
import com.mall.shopping.dto.AllProductCateResponse;
import com.mall.shopping.dto.ProductCateDto;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Component
public class ProductCateServiceImpl implements IProductCateService {

    @Autowired
    ItemCatMapper itemCatMapper;

    @Autowired
    ProductCateConverter productCateConverter;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public AllProductCateResponse getAllProductCate(AllProductCateRequest request) {
        log.warn("init com.mall.shopping.services.ProductCateServiceImpl.getAllProductCate request: ", request);
        AllProductCateResponse response = new AllProductCateResponse();
        //捕获所有异常
        try {
            //redis中寻找缓存数据，没有数据再去MySql查找
            RBucket<Object> bucket = redissonClient.getBucket(GlobalConstants.PRODUCT_CATE_CACHE_KEY);
            String jsonStr = (String) bucket.get();

            List<ProductCateDto> productCateDtos = null;
            //缓存数据判空
            if (StringUtils.isEmpty(jsonStr)) {
                //搜索条件
                Example example = new Example(ItemCat.class);
                example.setOrderByClause("sort_order" + " " +  request.getSort());

                List<ItemCat> itemCats = itemCatMapper.selectByExample(example);
//            List<ItemCat> itemCats = itemCatMapper.selectAll();

                productCateDtos = productCateConverter.items2Dto(itemCats);
                //
                bucket.set(JSON.toJSONString(productCateDtos), GlobalConstants.PRODUCT_CATE_EXPIRE_TIME, TimeUnit.MINUTES);
            }else{
                //取出缓存生成对应的dto对象
//                List list = JSON.parseObject(jsonStr, List.class);
//                JSONArray jsonArray = jsonObject.getJSONArray();
//                JSON.parseObject(JSON.toJSONString(jsonObject), )
                productCateDtos = JSONObject.parseArray(jsonStr, ProductCateDto.class);
            }

            response.setProductCateDtoList(productCateDtos);
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("com.mall.shopping.services.ProductCateServiceImpl.getAllProductCate occor Exception: {}", e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
