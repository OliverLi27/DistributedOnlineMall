package com.mall.shopping.services;

import com.alibaba.fastjson.JSON;
import com.mall.shopping.IContentService;
import com.mall.shopping.constant.GlobalConstants;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.converter.ProductCateConverter;
import com.mall.shopping.dal.entitys.ItemCat;
import com.mall.shopping.dal.entitys.PanelContent;
import com.mall.shopping.dal.persistence.ItemCatMapper;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.dto.NavListResponse;
import com.mall.shopping.dto.PanelContentDto;
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
public class ContentServiceImpl implements IContentService {

    @Autowired
    ItemCatMapper itemCatMapper;

    @Autowired
    ProductCateConverter productCateConverter;

    @Autowired
    ContentConverter contentConverter;

    @Autowired
    PanelContentMapper panelContentMapper;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public NavListResponse queryNavList() {
        log.warn("init ContentServiceImpl.queryNavList");
        NavListResponse response = new NavListResponse();
        try {
            //redis缓存数据取用
            RBucket<Object> bucket = redissonClient.getBucket(GlobalConstants.HEADER_PANEL_CACHE_KEY);
            String jsonStr = (String) bucket.get();
            List<PanelContentDto> panelContentDtos;
            //redis缓存数据判空
            if (StringUtils.isEmpty(jsonStr)) {
                //redis中无数据，去MySql中查找
                Example example = new Example(PanelContent.class);
                example.setOrderByClause("sort_order");
                example.createCriteria().andEqualTo("panelId", 0);

                List<PanelContent> panelContents = panelContentMapper.selectByExample(example);

                panelContentDtos = contentConverter.panelContents2Dto(panelContents);

                //转为json字串存入缓存中
                bucket.set(JSON.toJSONString(panelContentDtos));
            } else {
                //redis缓存有数据，取出转为dto对象
                panelContentDtos = JSON.parseArray(jsonStr, PanelContentDto.class);
            }
            response.setPannelContentDtos(panelContentDtos);
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("com.mall.shopping.services.ContentServiceImpl.queryNavList occor Exception: {}", e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
