package com.mall.shopping.converter;

import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dto.PanelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PanelConverter {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "sortOrder", target = "sortOrder"),
            @Mapping(source = "position", target = "position"),
            @Mapping(source = "limitNum", target = "limitNum"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "remark", target = "remark"),
            @Mapping(source = "panelContentItems", target = "panelContentItems")
    })
    PanelDto panel2Dto(Panel panel);

    List<PanelDto> panels2Dto(List<Panel> panels);
}
