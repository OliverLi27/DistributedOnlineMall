package com.mall.shopping.converter;

import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dto.CartProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.persistence.Table;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CartProductConverter {

    @Mappings({
            @Mapping(source = "id", target = "productId"),
            @Mapping(source = "price", target = "salePrice"),
            @Mapping(source = "limitNum", target = "limitNum"),
            @Mapping(source = "title", target = "productName"),
            @Mapping(source = "image", target = "productImg")
    })
    CartProductDto item2Dto(Item item);

    List<CartProductDto> itemList2Dto(List<CartProductDto> userCart);

}
