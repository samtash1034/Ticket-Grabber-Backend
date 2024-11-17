package com.project.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiPageRes<T> {

    @Schema(description = "回傳資料")
    T data;

    @Schema(description = "回傳資料總筆數")
    Integer totalCount = 0;
}
