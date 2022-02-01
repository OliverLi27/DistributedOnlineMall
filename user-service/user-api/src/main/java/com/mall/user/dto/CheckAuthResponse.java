package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 *  Xingchen Li
 * create-date: 2019/7/22-13:12
 */
@Data
public class CheckAuthResponse extends AbstractResponse {

    private Long uid;
}
