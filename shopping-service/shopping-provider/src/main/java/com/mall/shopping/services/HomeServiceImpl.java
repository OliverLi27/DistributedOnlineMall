package com.mall.shopping.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.shopping.IHomeService;
import com.mall.shopping.constant.GlobalConstants;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.converter.PanelConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dal.entitys.PanelContent;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.dal.persistence.PanelMapper;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.shopping.dto.PanelContentDto;
import com.mall.shopping.dto.PanelContentItemDto;
import com.mall.shopping.dto.PanelDto;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Component
public class HomeServiceImpl implements IHomeService {

    @Autowired
    PanelMapper panelMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    PanelContentMapper panelContentMapper;

    @Autowired
    PanelConverter panelConverter;

    @Autowired
    ContentConverter contentConverter;

    @Autowired
    RedissonClient redissonClient;


    @Override
    public HomePageResponse homepage() {
        log.warn("init HomeServiceImpl.homepage");
        HomePageResponse response = new HomePageResponse();

        try {
            //redis缓存中寻找是否有数据
            RBucket<Object> bucket = redissonClient.getBucket(GlobalConstants.HOMEPAGE_CACHE_KEY);
            String jsonStr = (String) bucket.get();
            List<PanelDto> panelDtos = null;
            //json数据为空判断，是否去MySql中查找
            if (StringUtils.isEmpty(jsonStr)) {
                List<Panel> panels = panelMapper.selectAll();
                panelDtos = panelConverter.panels2Dto(panels);

                for (PanelDto panelDto : panelDtos) {
                    Example panelContentExample = new Example(PanelContent.class);
                    panelContentExample.createCriteria().andEqualTo("panelId", panelDto.getId());

                    List<PanelContent> panelContents = panelContentMapper.selectByExample(panelContentExample);

                    List<PanelContentItemDto> panelContentItemDtos = contentConverter.panelContent2ItemDtos(panelContents);

                    for (PanelContentItemDto panelContentItemDto : panelContentItemDtos) {
                        Long productId = panelContentItemDto.getProductId();
                        Example itemExample = new Example(Item.class);
                        itemExample.createCriteria().andEqualTo("id", panelContentItemDto.getProductId());

                        Item item = itemMapper.selectByPrimaryKey(productId);
//                    List<Item> items = itemMapper.selectByExample(itemExample);

                        if (item == null) {
                            break;
                        }
                        panelContentItemDto.setSalePrice(item.getPrice());
                        panelContentItemDto.setSubTitle(item.getSellPoint());
                        panelContentItemDto.setProductName(item.getTitle());
                    }
                    panelDto.setPanelContentItems(panelContentItemDtos);

                }
                bucket.set(JSON.toJSONString(panelDtos), GlobalConstants.HOMEPAGE_EXPIRE_TIME, TimeUnit.MINUTES);
            }else{
                //jsonStr不为空，取出生成dto对象
                panelDtos = JSONObject.parseArray(jsonStr, PanelDto.class);
            }

            HashSet hashSet = new HashSet(panelDtos);
            response.setPanelContentItemDtos(hashSet);
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
        } catch (Exception e) {
            //捕获异常，生成日志，返回错误报文
            log.error("HomeServiceImpl.homepage occur Exception: {}", e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
