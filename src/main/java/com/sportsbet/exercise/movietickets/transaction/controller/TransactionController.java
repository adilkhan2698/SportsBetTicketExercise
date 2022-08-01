package com.sportsbet.exercise.movietickets.transaction.controller;

import com.sportsbet.exercise.movietickets.transaction.dto.request.TransactionCustomerRequest;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionCustomerResponse;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionTicketResponse;
import com.sportsbet.exercise.movietickets.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller for the transaction
 */
@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Controller method to accept post call of the transaction customers
     *
     * The return message contains the transactionCustomerId
     * If successful, return status created
     * If input validation fails, return status Bad Request
     */
    @PostMapping()
    public ResponseEntity<TransactionCustomerResponse> postTransactionCustomers(
            @RequestBody TransactionCustomerRequest transactionCustomerRequestDTO) {
        TransactionCustomerResponse transactionCustomerResponse
                =  transactionService.saveCustomerTransaction(transactionCustomerRequestDTO);
       return new ResponseEntity<>(transactionCustomerResponse, HttpStatus.CREATED);
    }

    /**
     * Controller method to accept get call of the transaction tickets
     *
     * The return message contains the TransactionTicketResponse
     * If successful, return status ok
     * If transacationCustomerId Not Present, returns Error as TransactionNotFound with status as Not Found
     */
    @GetMapping("/{id}/tickets")
    public TransactionTicketResponse getTransactionTickets(@PathVariable Integer id){
        return transactionService.getTransactionTicketDetails(id);
    }
}