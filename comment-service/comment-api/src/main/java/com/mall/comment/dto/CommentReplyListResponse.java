package com.mall.comment.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * @author Xingchen Li
 * @date 2019/8/22 23:36
 * 商品评价回复意见分页查询返回结果
 */
@Data
public class CommentReplyListResponse extends AbstractResponse {

    private long total;

    private List<CommentReplyDto> commentReplyDtoList;

    private int page;

    private int size;
}
