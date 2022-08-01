package com.sportsbet.exercise.movietickets.transaction.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Unit test for ticket type
 */
public class TicketTypeTest {

    /**
     * Test Senior ticket price is always 30 percent cheaper than adult
     */
    @Test
    public void testSeniorTicketPriceIsThirtyPercentLessThanADult(){
        Assert.isTrue(TicketType.SENIOR.getCost().equals(
                TicketType.ADULT.getCost().multiply(
                        new BigDecimal(0.70)).setScale(2, RoundingMode.HALF_UP)),"Senior Ticket price is not thirty percent cheaper than Adult");
    }

    /**
     * Test get ticket type function returns the correct ticket type based on the given age
     */
    @Test
    public void testGetTicketTypeBasedOnAge(){
        Assert.isNull(TicketType.getTicketType(-1), "Ticket age cannot be negative");
        Assert.isNull(TicketType.getTicketType(150), "Maximum Ticket age can only be 149");
        Assert.isTrue(TicketType.getTicketType(0).equals(TicketType.CHILDREN),"Ticket type getByAge mismatch, age 0. required Children");
        Assert.isTrue(TicketType.getTicketType(10).equals(TicketType.CHILDREN),"Ticket type getByAge mismatch, age 10, required Children");
        Assert.isTrue(TicketType.getTicketType(11).equals(TicketType.TEEN),"Ticket type getByAge mismatch, age 11, required Teen");
        Assert.isTrue(TicketType.getTicketType(17).equals(TicketType.TEEN),"Ticket type getByAge mismatch, age 17, required Teen");
        Assert.isTrue(TicketType.getTicketType(18).equals(TicketType.ADULT),"Ticket type getByAge mismatch, age 18, required Adult");
        Assert.isTrue(TicketType.getTicketType(64).equals(TicketType.ADULT),"Ticket type getByAge mismatch, age 64, required Adult");
        Assert.isTrue(TicketType.getTicketType(65).equals(TicketType.SENIOR),"Ticket type getByAge mismatch, age 65, required Senior");
        Assert.isTrue(TicketType.getTicketType(149).equals(TicketType.SENIOR),"Ticket type getByAge mismatch, age 149, required Senior");
    }
}
