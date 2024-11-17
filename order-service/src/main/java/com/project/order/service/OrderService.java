package com.project.order.service;

import com.project.order.response.OrderResponse;

public interface OrderService {

    OrderResponse getOrderById(String orderId);
}
