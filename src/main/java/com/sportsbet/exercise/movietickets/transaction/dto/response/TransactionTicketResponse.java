package com.sportsbet.exercise.movietickets.transaction.dto.response;

import com.sportsbet.exercise.movietickets.transaction.model.Ticket;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * Response data transfer object of get transaction tickets rest api
 */
@Getter @Setter
public class TransactionTicketResponse {
    private Integer transactionId;
    private Set<Ticket> tickets;

    private BigDecimal totalCost;

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;

        //set the total cost of the tickets
        if(tickets != null){
            this.setTotalCost(
                    tickets.stream().map(ticket -> ticket.getTotalCost())
                            .reduce(BigDecimal::add).get().setScale(2, RoundingMode.HALF_UP));
        }
    }

    /**
     * Setter for total cost
     * Reduced visibility as total cost is based on tickets
     * @param totalCost
     */
    private void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}