package com.sportsbet.exercise.movietickets.transaction.service;

import com.sportsbet.exercise.movietickets.transaction.constants.TicketType;
import com.sportsbet.exercise.movietickets.transaction.dto.request.TransactionCustomerRequest;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionCustomerResponse;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionTicketResponse;
import com.sportsbet.exercise.movietickets.transaction.model.Customer;
import com.sportsbet.exercise.movietickets.transaction.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Unit test for Transaction service
 */
public class TransactionServiceTest {

    TransactionService transactionService;

    @BeforeEach
    public void init(){
        transactionService = new TransactionService();
    }

    /**
     * Test transactionCustomerId is generated on successful customer save
     */
    @Test
    public void testTransactionCustomerIdIsGeneratedOnSaveCustomer(){
        final List<Customer> customers = new ArrayList<>();
        final Customer customer = new Customer("testCustomer", 2);
        customers.add(customer);

        TransactionCustomerRequest request = new TransactionCustomerRequest(
                1, customers);

        TransactionCustomerResponse response =
                transactionService.saveCustomerTransaction(request);

        Assert.notNull(response, "TransactionCustomerResponse cannot be null");
        Assert.notNull(response.getTransactionCustomerId(), "TransactionCustomerResponse.transactionCustomerId cannot be null");
    }

    /**
     * Test valid transaction details are returned
     */
    @Test
    public void testValidTransactionTicketDetailWithGivenTransactionCustomerId(){
        final List<Customer> customers = new ArrayList<>();
        final Customer customer = new Customer("testCustomer", 2);
        customers.add(customer);

        final int transactionId = 1;
        TransactionTicketResponse ticketResponse = getTransactionTicketResponse(transactionId, customers);

        Assert.notNull(ticketResponse, "TransactionTicketResponse cannot be null");
        Assert.notNull(ticketResponse.getTransactionId(), "TransactionTicketResponse.transactionId cannot be null");
        Assert.isTrue(ticketResponse.getTransactionId() == transactionId, "TransactionId does not match in request and response");
        Assert.notNull(ticketResponse.getTotalCost(), "TransactionTicketResponse.totalCost cannot be null");
        Assert.notEmpty(ticketResponse.getTickets(), "TransactionTicketResponse.tickets cannot be empty");
        Assert.isTrue(ticketResponse.getTickets().size() == 1, "TransactionTicketResponse.tickets not grouped on the basis of TicketType");
        Assert.isTrue(ticketResponse.getTotalCost().equals(new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP)), "Invalid transaction total cost");

        Ticket responseTicket = (Ticket)ticketResponse.getTickets().toArray()[0];

        Assert.isTrue(responseTicket.getTicketTypeConstant().equals(TicketType.CHILDREN), "Invalid Ticket type, should be children based on age 2");
        Assert.isTrue(responseTicket.getQuantity() == 1, "Invalid Ticket quantity");
        Assert.isTrue(responseTicket.getTotalCost().equals(new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP)), "Invalid Ticket total cost");
    }

    /**
     * Test Transaction Tickets are ordered alphabetically
     */
    @Test
    public void testTransactionTicketOrderedAlphabetically(){

        final Customer customer1 = new Customer("Billy Kidd", 36);
        final Customer customer2 = new Customer("Zoe Daniels", 3);
        final Customer customer3 = new Customer("George White", 8);
        final Customer customer4 = new Customer("Tommy Anderson", 9);
        final Customer customer5 = new Customer("Joe Smith", 17);

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);

        validateTicketOrder(customers);

        //change the order of customers
        customers = new ArrayList<>();
        customers.add(customer5);
        customers.add(customer4);
        customers.add(customer3);
        customers.add(customer2);
        customers.add(customer1);

        validateTicketOrder(customers);

    }

    /**
     * Test twenty five percent discount applied on three or more children tickets
     */
    @Test
    public void testTwentyFivePercentDiscountForThreeOrMoreChildren(){
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Billy Kidd", 36));
        customers.add(new Customer("Zoe Daniels", 3));
        customers.add(new Customer("George White", 8));
        customers.add(new Customer("Tommy Anderson", 9));
        customers.add(new Customer("Joe Smith", 17));

        TransactionTicketResponse ticketResponse = getTransactionTicketResponse(1, customers);
        Assert.isTrue(ticketResponse.getTickets().size() == 3, "TransactionTicketResponse.tickets not grouped on the basis of TicketType");

        List<Ticket> responseTickets = new ArrayList<>(ticketResponse.getTickets());

        Assert.isTrue(responseTickets.get(1).getTicketTypeConstant().equals(TicketType.CHILDREN), "TransactionTicket is not alphabetical order, required Children");

        Assert.isTrue(responseTickets.get(1).getTotalCost().equals(
                TicketType.CHILDREN.getCost().multiply(new BigDecimal(3))
                        .multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP)),"Twenty Five percent discount not applied for 3 or more children");

    }

    /**
     * Test tickets are grouped together by ticket type in the response
     */
    @Test
    public void testTicketResponseTicketsAreGroupedByType(){
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Billy Kidd", 36));
        customers.add(new Customer("Zoe Daniels", 3));
        customers.add(new Customer("George White", 8));
        customers.add(new Customer("Tommy Anderson", 9));
        customers.add(new Customer("Joe Smith", 17));

        TransactionTicketResponse ticketResponse = getTransactionTicketResponse(1, customers);
        Assert.isTrue(ticketResponse.getTickets().size() == 3, "TransactionTicketResponse.tickets not grouped on the basis of TicketType");

        List<Ticket> responseTickets = new ArrayList<>(ticketResponse.getTickets());

        Assert.isTrue(responseTickets.get(1).getQuantity() == 3, "TransactionTicket is not grouped by type");

    }

    /**
     * Test total cost in the response is the sum of the individual ticket sum
     */
    @Test
    public void testTotalCostIsSumOfIndividualTickets(){
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Jesse James", 36));
        customers.add(new Customer("Daniel Anderson", 95));
        customers.add(new Customer("Mary Jones", 15));
        customers.add(new Customer("Michelle Parker", 10));

        TransactionTicketResponse ticketResponse = getTransactionTicketResponse(1, customers);
        Assert.isTrue(ticketResponse.getTickets().size() == 4, "TransactionTicketResponse.tickets not grouped on the basis of TicketType");

        BigDecimal totalPrice = ticketResponse.getTickets().stream().map(Ticket::getTotalCost).reduce(BigDecimal::add).get().setScale(2, RoundingMode.HALF_UP);

        Assert.isTrue(totalPrice.equals(ticketResponse.getTotalCost()), "Sum of ticket and transaction total does not match");
    }

    /**
     * private method to validate the ticket order
     */
    private void validateTicketOrder(List<Customer> customers){
        TransactionTicketResponse ticketResponse = getTransactionTicketResponse(1, customers);
        Assert.isTrue(ticketResponse.getTickets().size() == 3, "TransactionTicketResponse.tickets not grouped on the basis of TicketType");
        Assert.isInstanceOf(TreeSet.class, ticketResponse.getTickets());

        List<Ticket> responseTickets = ticketResponse.getTickets().stream().collect(Collectors.toList());

        Assert.isTrue(responseTickets.get(0).getTicketTypeConstant().equals(TicketType.ADULT), "TransactionTicket is not alphabetical order, required Adult");
        Assert.isTrue(responseTickets.get(1).getTicketTypeConstant().equals(TicketType.CHILDREN), "TransactionTicket is not alphabetical order, required Children");
        Assert.isTrue(responseTickets.get(2).getTicketTypeConstant().equals(TicketType.TEEN), "TransactionTicket is not alphabetical order, required Teen");

    }

    /**
     * To get the transaction ticket response based on the given customers and transactionId
     *
     * @param transactionId, int
     * @param customers List<Customer>
     * @return TransactionTicketResponse
     */
    private TransactionTicketResponse getTransactionTicketResponse(int transactionId, List<Customer> customers){
        TransactionCustomerRequest request = new TransactionCustomerRequest(
                transactionId, customers);

        TransactionCustomerResponse customerResponse =
                transactionService.saveCustomerTransaction(request);

        return transactionService.getTransactionTicketDetails(customerResponse.getTransactionCustomerId());

    }

}