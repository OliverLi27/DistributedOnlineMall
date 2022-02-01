package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/17 9:14
 */
@Data
public class LoginVerifyResponse extends AbstractResponse {
    private Long uid;
    private String file;
}
