package com.sportsbet.exercise.movietickets.transaction.service;

import com.sportsbet.exercise.movietickets.transaction.constants.TicketType;
import com.sportsbet.exercise.movietickets.transaction.dto.request.TransactionCustomerRequest;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionCustomerResponse;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionTicketResponse;
import com.sportsbet.exercise.movietickets.transaction.model.Customer;
import com.sportsbet.exercise.movietickets.transaction.model.Ticket;
import com.sportsbet.exercise.movietickets.transaction.validation.exception.TransactionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service class for transaction
 */
@Validated
@Service
public class TransactionService {

    // map will hold a customer transaction details
    //key will be an identifier which will signify a particular transaction customer object received
    //value will be the transactionCustomer object
    private final Map<Integer, TransactionCustomerRequest> customerTransactions
            = new HashMap<>();

    //identity to be used as
    private final AtomicInteger currentId = new AtomicInteger(1);

    /**
     * Save Customer Transaction Details
     * Method validates the input parameters
     * @param transactionCustomerRequest, TransactionCustomerRequest received
     * @return, TransactionCustomerResponse contains the transactionCustomerID
     */
    public TransactionCustomerResponse saveCustomerTransaction(@Valid TransactionCustomerRequest transactionCustomerRequest){
        int currentId = this.currentId.getAndIncrement();
        customerTransactions.put(currentId
                , transactionCustomerRequest);
        return new TransactionCustomerResponse(currentId);
    }


    /**
     * To get the ticket details based on the transaction customer id provided
     * It validates the input parameters and throws exception in case of unsuccessful validation
     *
     * @param transactionCustomerId, Transaction Customer id which was generated when the customer details were saved
     *
     * @return TransactionTicketResponse
     *
     * @throws TransactionNotFoundException, in case transaction details not found
     */
    public TransactionTicketResponse getTransactionTicketDetails(@NotNull Integer transactionCustomerId) {

        // find the corresponding request object
        TransactionCustomerRequest request
                = customerTransactions.get(transactionCustomerId);

        if(null != request){
            //request object found
            List<Customer> customers = request.getCustomers();

            //Create a set of tickets based on alphabetical order
            final  Set<Ticket> transactionTickets = new TreeSet<>(
                    Comparator.comparing(ticket -> ticket.getTicketTypeConstant().getType())
            );

            //group tickets by type and add quantity for each new record found in same type
            customers.stream()
                    .map(
                            customer ->  new Ticket(TicketType.getTicketType(customer.getAge()),1) )
                    .forEach(ticket -> {
                        if(transactionTickets.contains(ticket)){
                            //ticket already exist, add quantity
                            Ticket existingTicket
                                    = transactionTickets.stream().filter(t1 -> t1.equals(ticket)).findFirst().get();
                            existingTicket.setQuantity(existingTicket.getQuantity()+1);
                        }else{
                            // ticket not exist
                           transactionTickets.add(ticket);
                        }
                    });

            //generate the response dto
            TransactionTicketResponse transactionTicketResponseDTO = new TransactionTicketResponse();

            // transaction id is the generated customer transaction id Not the transaction
            //id send in the customer request
            transactionTicketResponseDTO.setTransactionId(transactionCustomerId);
            transactionTicketResponseDTO.setTickets(transactionTickets);

            return transactionTicketResponseDTO;

        }else{
            //throw TransactionNotFoundException, if transaction details not found
            throw new TransactionNotFoundException("No transaction present with the given transaction id " + transactionCustomerId);
        }
    }
}