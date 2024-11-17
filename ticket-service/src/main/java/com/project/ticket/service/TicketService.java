package com.project.ticket.service;

import com.project.ticket.request.GrabTicketRequest;
import com.project.ticket.response.GrabTicketResponse;
import com.project.ticket.response.RemainingSeatResponse;

public interface TicketService {

    GrabTicketResponse grabTicket(GrabTicketRequest req, String userId);

    RemainingSeatResponse getRemainingSeats(String concertId, String seatArea);
}
