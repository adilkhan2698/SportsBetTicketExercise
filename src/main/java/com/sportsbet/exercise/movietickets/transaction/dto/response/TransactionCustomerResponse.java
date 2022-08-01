package com.sportsbet.exercise.movietickets.transaction.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Response data transfer object of post transaction rest api
 */
@Getter @Setter
public class TransactionCustomerResponse {

    private Integer transactionCustomerId;

    public TransactionCustomerResponse(Integer transactionCustomerId) {
        this.transactionCustomerId = transactionCustomerId;
    }

    public TransactionCustomerResponse(){}


}
