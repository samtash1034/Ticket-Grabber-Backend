package com.project.orm.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConcertMapper {

    int selectAvailableSeatByConcertId(String concertId);

    int decrementAvailableSeatByConcertId(String concertId);

}
