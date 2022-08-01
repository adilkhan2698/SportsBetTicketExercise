package com.sportsbet.exercise.movietickets.transaction.dto.request;

import com.sportsbet.exercise.movietickets.transaction.model.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request Data Object for transaction Customers
 */
@Getter @Setter
public class TransactionCustomerRequest {
    private int transactionId;

    @NotEmpty @Valid
    private List<Customer> customers;

    public TransactionCustomerRequest(int transactionId, List<Customer> customers) {
        this.transactionId = transactionId;
        this.customers = customers;
    }
}
