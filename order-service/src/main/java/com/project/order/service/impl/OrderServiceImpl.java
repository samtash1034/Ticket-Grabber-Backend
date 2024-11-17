package com.project.order.service.impl;

import com.project.common.enums.CommonCode;
import com.project.common.exception.BaseException;
import com.project.order.response.OrderDetailResponse;
import com.project.order.response.OrderResponse;
import com.project.order.service.OrderService;
import com.project.orm.mapper.OrderMapper;
import com.project.orm.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponse getOrderById(String orderId) {
        String status = redisTemplate.opsForValue().get("concertOrderStatus:" + orderId);

        if(Objects.equals(status, "PENDING")) {
            throw new BaseException(CommonCode.N40008);
        } else if (!Objects.equals(status, "SUCCESS")) {
            throw new BaseException(CommonCode.N40009);
        }
            //        else {
//            throw new BaseException(CommonCode.N40008);
//
//        }

        List<OrderModel> orderModelList = orderMapper.selectByOrderId(orderId);

        OrderResponse response = new OrderResponse();
        response.setConcertName("庫拉皮卡丘演唱會");
        response.setOrderId(orderId);
        response.setCreateTime(orderModelList.get(0).getCreateTime());

        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderModel order : orderModelList) {
            OrderDetailResponse detail = new OrderDetailResponse();
            detail.setOrderNo(order.getOrderNo());
            detail.setSeat(order.getSeat());
            detailResponses.add(detail);
        }

        response.setDetailResponses(detailResponses);

        return response;
    }
}
