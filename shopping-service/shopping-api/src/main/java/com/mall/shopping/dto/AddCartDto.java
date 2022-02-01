package com.mall.shopping.dto;

import lombok.Data;

@Data
public class AddCartDto {

    private Long productId;

    private Integer productNum;

    private Long userId;

}
