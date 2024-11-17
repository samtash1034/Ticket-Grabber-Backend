package com.project.orm.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderModel {

    private String orderId;

    private Integer orderNo;

    private String userId;

    private String concertId;

    private String seat;

    private String orderStatus;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer timeDiffSeconds;
}
