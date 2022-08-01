package com.sportsbet.exercise.movietickets.transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sportsbet.exercise.movietickets.transaction.constants.TicketType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
/**
 * Model for Ticket
 */
public class Ticket {

    @NotNull @JsonIgnore
    private TicketType ticketTypeConstant;

    private String ticketType;

    @Min(1)
    private int quantity;

    private BigDecimal totalCost;

    public Ticket(TicketType ticketTypeConstant, int quantity) {
        this.ticketTypeConstant = ticketTypeConstant;
        this.quantity = quantity;
        this.ticketType = ticketTypeConstant.getType();

        //update total cost
        this.setTotalCost();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        //update total cost after updating quantity
        this.setTotalCost();
    }

    private void setTotalCost(){
        if(null != ticketType && quantity > 0){
            this.totalCost = getTotalCostAfterDiscount(
                    ticketTypeConstant.getCost().multiply(new BigDecimal(quantity)));
        }else {
            this.totalCost = new BigDecimal(0).setScale(2);
        }
    }

    private BigDecimal getTotalCostAfterDiscount(BigDecimal totalCost){
        if(quantity >= 3 && ticketTypeConstant.getType().equalsIgnoreCase(
                TicketType.CHILDREN.getType())){

            //apply 25 % discount
            return totalCost.multiply(new BigDecimal(100-25)).divide(new BigDecimal(100));
        }
        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return ticketType == ticket.ticketType;
    }

}
