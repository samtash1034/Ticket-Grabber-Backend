package com.project.order.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    private String orderId;

    private String concertName;

    private Date createTime;

    private List<OrderDetailResponse> detailResponses;
}
