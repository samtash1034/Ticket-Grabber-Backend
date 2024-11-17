package com.project.ticket.producer;

import com.project.common.message.ConcertOrderMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcertOrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(ConcertOrderMessage message) {
        rabbitTemplate.convertAndSend("concertOrderQueue", message);
    }
}
