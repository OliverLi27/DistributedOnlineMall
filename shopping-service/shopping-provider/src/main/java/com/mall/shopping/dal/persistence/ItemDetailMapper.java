package com.mall.shopping.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.shopping.dal.entitys.ItemDetail;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: chengxin
 * @Date :  2020/9/17 11:01
 * @Version 1.0
 */
public interface ItemDetailMapper extends TkMapper<ItemDetail> {

    ItemDetail selectProductDetail(@Param("id") Long id);
}
