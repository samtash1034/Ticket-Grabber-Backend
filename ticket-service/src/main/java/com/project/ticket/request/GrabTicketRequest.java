package com.project.ticket.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GrabTicketRequest {

    @Schema(description = "演唱會id")
    private String concertId;

    @Schema(description = "座位區域（A, B, C)")
    private String seatArea;

    @Schema(description = "票數（最多4張)")
    private Integer quantity;
}
