package com.mall.comment.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author Xingchen Li
 * @date 2019/8/17 23:16
 */
@Data
public class TotalCommentResponse extends AbstractResponse {

    private long total;
}
