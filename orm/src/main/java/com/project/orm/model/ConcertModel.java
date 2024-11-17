package com.project.orm.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ConcertModel {

    private Long concertId;

    private String name;

    private Timestamp date;

    private String location;

    private Integer availableSeat;

    private Integer totalSeat;

    private Timestamp createTime;
}
