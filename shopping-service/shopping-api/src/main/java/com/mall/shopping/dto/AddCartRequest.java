package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.shopping.constants.ShoppingRetCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Xingchen Li on 2019/7/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCartRequest extends AbstractRequest{

    private Long userId;
    private Long itemId;
    private Integer num;

    @Override
    public void requestCheck() {
        if (userId == null && itemId == null && num == null) {
            throw new ValidateException(
                    ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),
                    ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }
    }
}
