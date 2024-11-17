package com.project.order.consumer;

import com.project.common.message.ConcertOrderMessage;
import com.project.common.util.TimestampUtil;
import com.project.orm.mapper.OrderMapper;
import com.project.orm.model.OrderModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConcertOrderConsumer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 先把訂單存到 redis，之後透過排程新增到 db
     */
    @Transactional
    @RabbitListener(queues = "concertOrderQueue")
    public void receiveOrder(ConcertOrderMessage message) {
        String orderId = message.getOrderId();
        List<String> seatList = message.getSeatList();
        LocalDateTime requestTime = message.getCreateTime();
        try {

            for (int i = 0; i < seatList.size(); i++) {
                Timestamp now = TimestampUtil.getCurrentTimestamp();
                OrderModel orderModel = new OrderModel();
                orderModel.setOrderId(orderId);
                orderModel.setOrderNo(i + 1);
                orderModel.setUserId(message.getUserId());
                orderModel.setConcertId(message.getConcertId());
                orderModel.setSeat(seatList.get(i));
                orderModel.setOrderStatus("SUCCESS");
                orderModel.setCreateTime(now);
                orderModel.setUpdateTime(now);

                // 計算時間差
                long diffSeconds = Duration.between(requestTime, now.toLocalDateTime()).getSeconds();
                orderModel.setTimeDiffSeconds((int) diffSeconds);

                orderMapper.insert(orderModel);
            }

            // 更新訂單狀態為 SUCCESS
            redisTemplate.opsForValue().set("concertOrderStatus:" + orderId, "SUCCESS");
        } catch (Exception e) {
            // 更新訂單狀態為 FAILED
            System.out.println("訂單插入到資料庫失敗");
            redisTemplate.opsForValue().set("concertOrderStatus:" + orderId, "FAILED");
            throw e; // 確保交易回滾
        }

    }
}
