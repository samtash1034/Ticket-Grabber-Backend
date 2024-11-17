package com.project.ticket.service.impl;

import com.project.common.enums.CommonCode;
import com.project.common.exception.BaseException;
import com.project.common.message.ConcertOrderMessage;
import com.project.common.util.UUIDUtil;
import com.project.orm.mapper.ConcertMapper;
import com.project.orm.mapper.OrderMapper;
import com.project.ticket.producer.ConcertOrderProducer;
import com.project.ticket.request.GrabTicketRequest;
import com.project.ticket.response.GrabTicketResponse;
import com.project.ticket.response.RemainingSeatResponse;
import com.project.ticket.service.TicketService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private ConcertMapper concertMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ConcertOrderProducer concertOrderProducer;

    private final String concert_1A_Key = "concert:1-A-{k2}";
    private final String concert_1B_Key = "concert:1-B-{k4}";
    private final String concert_1C_Key = "concert:1-C-{k5}";

    private DefaultRedisScript<List> multiLeftPopScript;

    @PostConstruct
    public void init() {
        // delete All keys
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        List<String> listA = createList("A");
        List<String> listB = createList("B");
        List<String> listC = createList("C");

        redisTemplate.opsForList().rightPushAll(concert_1A_Key, listA);
        redisTemplate.opsForList().rightPushAll(concert_1B_Key, listB);
        redisTemplate.opsForList().rightPushAll(concert_1C_Key, listC);

        orderMapper.deleteAll();

        // 加載 Lua 腳本
        multiLeftPopScript = new DefaultRedisScript<>();
        multiLeftPopScript.setLocation(new ClassPathResource("scripts/concert_multi_left_pop.lua"));
        multiLeftPopScript.setResultType(List.class);
    }

    private List<String> createList(String prefix) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            list.add(prefix + i);
        }
        return list;
    }

    @Override
    @Transactional
    public GrabTicketResponse grabTicket(GrabTicketRequest req, String userId) {
        // method1(req);
        return method2(req, userId);
    }

    @Override
    public RemainingSeatResponse getRemainingSeats(String concertId, String seatArea) {
        String seatAreaKey = getSeatAreaKey(seatArea, concertId);

        Long size = redisTemplate.opsForList().size(seatAreaKey);
        RemainingSeatResponse response = new RemainingSeatResponse();
        response.setRemainingSeat(size);

        return response;
    }

    private void method1(GrabTicketRequest req) {
        // 一次只有一個人能執行下面這段程式
        // synchronized 只有在單機有用
        synchronized (this) {
            int availableSeat = concertMapper.selectAvailableSeatByConcertId(req.getConcertId());
            System.out.println("剩餘座位數量：" + availableSeat);

            if (availableSeat > 0) {
                concertMapper.decrementAvailableSeatByConcertId(req.getConcertId());
            } else {
                throw new BaseException(CommonCode.N40007, "沒有剩餘的的座位");
            }
        }
    }


    private GrabTicketResponse method2(GrabTicketRequest req, String userId) {
        GrabTicketResponse response = new GrabTicketResponse();

        String seatArea = req.getSeatArea();
        String concertId = req.getConcertId();
        Integer quantity = req.getQuantity();

        String seatAreaKey = getSeatAreaKey(seatArea, concertId);

        // 使用 Lua 腳本一次性彈出多個座位
        List<String> seatList = redisTemplate.execute(
                multiLeftPopScript,
                Arrays.asList(seatAreaKey), // 將鍵作為列表傳遞
                quantity.toString()          // 作為參數傳遞
        );

        boolean isSuccessful = seatList != null && seatList.size() == quantity;

        if (isSuccessful) {
            // 創建並發送訂單消息
            ConcertOrderMessage message = new ConcertOrderMessage();
            String orderId = UUIDUtil.generateUuid();

            message.setOrderId(orderId);
            message.setUserId(userId);
            message.setConcertId(concertId);
            message.setSeatList(seatList);
            message.setCreateTime(LocalDateTime.now());

            redisTemplate.opsForValue().set("concertOrderStatus:" + orderId, "PENDING");

            response.setOrderId(orderId);

            concertOrderProducer.sendMessage(message);
        } else {
            // 若無法獲取全部座位，將已取出的座位放回 Redis
            if (seatList != null && !seatList.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(seatAreaKey, seatList);
            }

            System.out.println("剩餘座位不足，無法購買所需的數量");
            throw new BaseException(CommonCode.N40007, "剩餘座位不足，無法購買所需的數量");
        }

        return response;
    }

    private String getSeatAreaKey(String seatArea, String concertId) {
        if (concertId.equals("1")) {
            return switch (seatArea) {
                case "A" -> concert_1A_Key;
                case "B" -> concert_1B_Key;
                case "C" -> concert_1C_Key;
                default -> throw new BaseException(CommonCode.N40007, "座位區域異常");
            };
        } else {
            throw new BaseException(CommonCode.N40007, "無效的演唱會 ID");
        }
    }
}
