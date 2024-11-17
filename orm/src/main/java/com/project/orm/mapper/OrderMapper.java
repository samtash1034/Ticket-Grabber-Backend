package com.project.orm.mapper;

import com.project.orm.model.OrderModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(OrderModel orderModel);

    void deleteAll();

    List<OrderModel> selectByOrderId(String orderId);
}
