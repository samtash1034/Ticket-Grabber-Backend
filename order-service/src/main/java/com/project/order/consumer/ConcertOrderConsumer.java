package com.project.order.consumer;

import com.project.common.message.ConcertOrderMessage;
import com.project.common.util.TimestampUtil;
import com.project.orm.mapper.OrderMapper;
import com.project.orm.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ConcertOrderConsumer {

    private static final String ORDER_STATUS_KEY_PREFIX = "concertOrderStatus:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 接收並處理演唱會訂單訊息
     */
    @Transactional
    @RabbitListener(queues = "concertOrderQueue")
    public void receiveOrder(ConcertOrderMessage message) {
        String orderId = message.getOrderId();
        List<String> seatList = message.getSeatList();
        LocalDateTime requestTime = message.getCreateTime();

        try {
            for (int i = 0; i < seatList.size(); i++) {
                processSeatOrder(message, seatList.get(i), i + 1, requestTime);
            }

            // 更新 Redis 中的訂單狀態為 SUCCESS
            updateOrderStatusInRedis(orderId, "SUCCESS");
        } catch (Exception e) {
            log.error("訂單處理失敗，orderId: {}", orderId, e);

            // 更新 Redis 中的訂單狀態為 FAILED
            updateOrderStatusInRedis(orderId, "FAILED");

            // 確保交易回滾
            throw e;
        }
    }


    private void processSeatOrder(ConcertOrderMessage message, String seat, int orderNo, LocalDateTime requestTime) {
        Timestamp now = TimestampUtil.getCurrentTimestamp();
        OrderModel orderModel = buildOrderModel(message, seat, orderNo, requestTime, now);
        orderMapper.insert(orderModel);
    }


    private OrderModel buildOrderModel(ConcertOrderMessage message, String seat, int orderNo, LocalDateTime requestTime, Timestamp now) {
        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(message.getOrderId());
        orderModel.setOrderNo(orderNo);
        orderModel.setUserId(message.getUserId());
        orderModel.setConcertId(message.getConcertId());
        orderModel.setSeat(seat);
        orderModel.setOrderStatus("SUCCESS");
        orderModel.setCreateTime(now);
        orderModel.setUpdateTime(now);

        // 計算時間差
        long diffSeconds = Duration.between(requestTime, now.toLocalDateTime()).getSeconds();
        orderModel.setTimeDiffSeconds((int) diffSeconds);

        return orderModel;
    }

    /**
     * 更新 Redis 中的訂單狀態
     */
    private void updateOrderStatusInRedis(String orderId, String status) {
        String redisKey = ORDER_STATUS_KEY_PREFIX + orderId;
        redisTemplate.opsForValue().set(redisKey, status);
    }
}
