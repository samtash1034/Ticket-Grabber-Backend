package com.project.common.message;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConcertOrderMessage implements Serializable {

    private String orderId;

    private String userId;

    private String concertId;

    private LocalDateTime createTime;

    private List<String> seatList;
}
