package com.project.order.controller;

import com.project.common.annotation.SkipTokenVerification;
import com.project.common.controller.BaseController;
import com.project.common.response.ApiRes;
import com.project.order.response.OrderResponse;
import com.project.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SkipTokenVerification
@RequestMapping("/api/order")
@RestController
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    @Operation(summary = "搶票")
    public ApiRes<Object> getOrderById(@PathVariable String orderId) {
        OrderResponse response = orderService.getOrderById(orderId);

        return handleResponse(response);
    }
}
