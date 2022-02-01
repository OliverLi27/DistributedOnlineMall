package com.mall.shopping.dto;

import lombok.Data;

import java.util.List;
@Data
public class AllProductDto {
    private List<ProductDto> data;

    private Long total;
}
