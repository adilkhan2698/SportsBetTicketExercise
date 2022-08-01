package com.sportsbet.exercise.movietickets.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsbet.exercise.movietickets.SportsBetApplication;
import com.sportsbet.exercise.movietickets.transaction.dto.request.TransactionCustomerRequest;
import com.sportsbet.exercise.movietickets.transaction.dto.response.TransactionCustomerResponse;
import com.sportsbet.exercise.movietickets.transaction.model.Customer;
import com.sportsbet.exercise.movietickets.transaction.validation.exception.TransactionNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for transaction controller api
 */
@SpringBootTest(classes = SportsBetApplication.class)
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Returns the sample TransactionCustomerRequest
     * @return, TransactionCustomerRequest
     */
    private TransactionCustomerRequest getRequest(){
        final List<Customer> customers = new ArrayList<Customer>();
        final Customer customer = new Customer("testCustomer", 2);
        customers.add(customer);

        return new TransactionCustomerRequest(
                1, customers);
    }

    /**
     * Test the Created status is returned on successful post request
     * The response should have the transaction customer request id generated
     */
    @Test
    public void returnCreatedStatusWhenCustomerRequestPosted() throws Exception {
        TransactionCustomerRequest request = getRequest();

        String requestJsonStr = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/transaction")
                        .content(requestJsonStr)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionCustomerId").exists());
    }

    /**
     * Test Bad request is returned on validation failure, customer details are not present
     */
    @Test
    public void returnBadRequestWhenCustomerDetailsNotPresent() throws Exception {
        TransactionCustomerRequest request = new TransactionCustomerRequest(
                0, new ArrayList<Customer>()
        );

        String requestJsonStr = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/transaction")
                        .content(requestJsonStr)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].fieldName").value("saveCustomerTransaction.transactionCustomerRequest.customers"))
                .andExpect(jsonPath("$[0].message").value("must not be empty"));
    }

    /**
     * Test Bad request is returned on validation failure, customer age is negative
     */
    @Test
    public void returnBadRequestWhenCustomerAgeLessThanZero() throws Exception {
        TransactionCustomerRequest request = getRequest();
        request.getCustomers().get(0).setAge(-1);


        String requestJsonStr = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/transaction")
                        .content(requestJsonStr)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].fieldName").value("saveCustomerTransaction.transactionCustomerRequest.customers[0].age"))
                .andExpect(jsonPath("$[0].message").value("must be greater than or equal to 0"));
    }

    /**
     * Test Bad request is returned on validation failure, customer age is null
     */
    @Test
    public void returnBadRequestWhenCustomerAgeIsNull() throws Exception {
        TransactionCustomerRequest request = getRequest();
        request.getCustomers().get(0).setAge(null);


        String requestJsonStr = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/transaction")
                        .content(requestJsonStr)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].fieldName").value("saveCustomerTransaction.transactionCustomerRequest.customers[0].age"))
                .andExpect(jsonPath("$[0].message").value("must not be null"));
    }

    /**
     * Test Bad request is returned on validation failure, customer name is not present
     */
    @Test
    public void returnBadRequestWhenCustomerNameIsBlank() throws Exception {
        TransactionCustomerRequest request = getRequest();
        request.getCustomers().get(0).setName(null);


        String requestJsonStr = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/transaction")
                        .content(requestJsonStr)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].fieldName").value("saveCustomerTransaction.transactionCustomerRequest.customers[0].name"))
                .andExpect(jsonPath("$[0].message").value("must not be blank"));
    }

    /**
     * Test correct ticket details are returned with the correct customer transaction id provided
     */
    @Test
    public void getTicketDetailsWithTransactionCustomerIdWhenTheCustomerDetailsArePosted() throws Exception {
        TransactionCustomerRequest request = getRequest();

        String requestJsonStr = objectMapper.writeValueAsString(request);

        String content = mockMvc.perform(
                post("/transaction")
                        .content(requestJsonStr)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        TransactionCustomerResponse response = objectMapper.readValue(
                content, TransactionCustomerResponse.class);

        String expectedRes = "{\"transactionId\":1,\"tickets\":[{\"ticketType\":\"Children\",\"quantity\":1,\"totalCost\":5.00}],\"totalCost\":5.00}";
        mockMvc.perform(
                get("/transaction/" + response.getTransactionCustomerId()+"/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedRes));
    }

    /**
     * Test Not Found is returned on validation failure, transaction details are not present
     * with the given customerTransactionId
     * Also test the validation failure message
     */
    @Test
    public void getNotFoundErrorForTicketDetailsWithInvalidTransactionCustomerId() throws Exception {
        int randomTransactionId = new Random().nextInt();
        mockMvc.perform(
                get("/transaction/" + randomTransactionId+"/tickets"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assert.isInstanceOf(TransactionNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assert.isTrue(
                        result.getResolvedException().getMessage().equals(
                                "No transaction present with the given transaction id " + randomTransactionId)
                        , "Invalid Transaction Not Found message"));
    }
}
