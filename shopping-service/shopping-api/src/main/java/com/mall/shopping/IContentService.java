package com.mall.shopping;

import com.mall.shopping.dto.NavListResponse;


public interface IContentService {

    /**
     * 导航栏信息
     * 1. 商品类目为父类目
     * 2. 获取类目的图标信息
     * @return
     */
    NavListResponse queryNavList();
}
