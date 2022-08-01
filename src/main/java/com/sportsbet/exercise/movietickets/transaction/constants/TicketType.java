package com.sportsbet.exercise.movietickets.transaction.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Enum containing the details of all ticket types
 */
@Getter
public enum TicketType {

    CHILDREN("Children", new BigDecimal("5.0"), 0, 11),
    TEEN("Teen", new BigDecimal("12.0"), 11, 18),
    ADULT("Adult", new BigDecimal("25.0"), 18, 65),
    //Senior cost will always be 70 % of the adult cost
    SENIOR("Senior", ADULT.cost.multiply(new BigDecimal(0.70)).setScale(2, RoundingMode.HALF_UP), 65, 150);

    private TicketType(String type,  BigDecimal cost, int startAge, int lessThanAge){
        this.type = type;
        this.cost = cost;
        this.startAge = startAge;
        this.lessThanAge = lessThanAge;
    }

    /**
     * Type of the ticket children, adult
     */
    private String type;

    /**
     * Cost of the individual ticket
     */
    @JsonIgnore
    private BigDecimal cost;

    /**
     * Start Age to which this ticket category belongs
     */
    @JsonIgnore
    private int startAge;

    /**
     * Less than age to which this ticket category belongs
     */
    @JsonIgnore
    private int lessThanAge;

    /**
     * Get the ticket type based on the age given
     * @param age, age corresponding to which the ticket type will be returned
     * @return type of the ticket if found otherwise null
     */
    public static TicketType getTicketType(int age){
        for(TicketType ticketType : TicketType.values()){
            if(age>=ticketType.startAge && age<ticketType.lessThanAge){
                return ticketType;
            }
        }
        return null;
    }
}
