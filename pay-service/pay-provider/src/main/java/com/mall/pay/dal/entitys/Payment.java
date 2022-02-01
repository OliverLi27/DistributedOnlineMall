package com.mall.pay.dal.entitys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/21 18:40
 */
@Table(name = "tb_payment")

@Data
public class Payment {
    @Id
    private String id;

    private Integer status;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_name")
    private String productName;
    @Column(name = "pay_no")
    String payNo;
    @Column(name = "trade_no")
    String tradeNo;

    @Column(name = "payer_uid")
    private  Integer uid;

    @Column(name = "payer_name")
    String payerName;

    @Column(name = "payer_amount")
    BigDecimal payerAmount;

    @Column(name = "order_amount")
    BigDecimal orderAmount;

    @Column(name = "pay_way")
    String payWay;

    @Column(name = "pay_success_time")
    Date successTime;

    @Column(name = "complete_time")
    Date completeTime;

    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
