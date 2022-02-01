package com.mall.shopping.converter;

import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dal.entitys.PanelContent;
import com.mall.shopping.dal.entitys.PanelContentItem;
import com.mall.shopping.dto.PanelContentDto;
import com.mall.shopping.dto.PanelContentItemDto;
import com.mall.shopping.dto.PanelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Xingchen Li
 * create-date: 2019/7/23-16:37
 */
@Mapper(componentModel = "spring")
public interface ContentConverter {

    @Mappings({})
    PanelContentDto panelContent2Dto(PanelContent panelContent);

    @Mappings({})
    PanelContentDto panelContentItem2Dto(PanelContentItem panelContentItem);

    @Mappings({})
    PanelDto panen2Dto(Panel panel);

    List<PanelContentDto> panelContents2Dto(List<PanelContent> panelContents);

    List<PanelContentItemDto> panelContentItem2Dto(List<PanelContentItem> panelContentItems);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "panelId", target = "panelId"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "productId", target = "productId"),
            @Mapping(source = "sortOrder", target = "sortOrder"),
            @Mapping(source = "fullUrl", target = "fullUrl"),
            @Mapping(source = "picUrl", target = "picUrl"),
            @Mapping(source = "picUrl2", target = "picUrl2"),
            @Mapping(source = "picUrl3", target = "picUrl3"),
            @Mapping(source = "created", target = "created"),
            @Mapping(source = "updated", target = "updated")
    })
    PanelContentItemDto panelContent2itemDto(PanelContent panelContent);

    List<PanelContentItemDto> panelContent2ItemDtos(List<PanelContent> panelContents);
    @Mappings({
            @Mapping(source = "title", target = "productName"),
            @Mapping(source = "price", target = "salePrice"),
            @Mapping(source = "sellPoint", target = "subTitle")
    })
    PanelContentItemDto item2itemDto(Item item);

    List<PanelContentItemDto> items2ItemDtos(List<Item> items);


}
