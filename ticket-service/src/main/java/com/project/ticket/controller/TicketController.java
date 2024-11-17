package com.project.ticket.controller;

import com.project.common.annotation.SkipTokenVerification;
import com.project.common.controller.BaseController;
import com.project.common.response.ApiRes;
import com.project.common.util.UUIDUtil;
import com.project.ticket.request.GrabTicketRequest;
import com.project.ticket.response.GrabTicketResponse;
import com.project.ticket.response.RemainingSeatResponse;
import com.project.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@SkipTokenVerification
@RequestMapping("/api/ticket")
@RestController
public class TicketController extends BaseController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/grab")
    @Operation(summary = "搶票")
    public ApiRes<Object> grabTicket(@RequestBody GrabTicketRequest req) {
        if (userId == null) {
            userId = UUIDUtil.generateUuid();
        }

        // 如果quantity為null，隨機生成1到4的數量
        if (req.getQuantity() == null) {
            req.setQuantity(ThreadLocalRandom.current().nextInt(1, 5));
        }

        // 如果seatArea為null，隨機選擇A、B、C區
        if (req.getSeatArea() == null) {
            String[] areas = {"A", "B", "C"};
            req.setSeatArea(areas[ThreadLocalRandom.current().nextInt(areas.length)]);
        }

        GrabTicketResponse response = ticketService.grabTicket(req, userId);

        return handleResponse(response);
    }

    @GetMapping("/seat")
    @Operation(summary = "取得剩餘座位")
    public ApiRes<Object> getRemainingSeats(@RequestParam String concertId, @RequestParam String seatArea) {
        RemainingSeatResponse response = ticketService.getRemainingSeats(concertId, seatArea);

        return handleResponse(response);
    }

}
