# SportsBetTicketExercise

The exercise is based on Spring Boot framework

-------------------------------------------------------------------------

There are two API's created

1. Post -> /transaction
This will return the TransactionCustomerId, which is the generated Id at the server for input request containing the customer details

2. Get -> /transaction/<TransactionCustomerId>/tickets
This will return the Tickets corresponding to the customers details posted. The TransactionCustomerId in the path is the id returned in the post call
---------------------------------------------------------------------------

The exercise contains the Unit tests and Integration Tests

---------------------------------------------------------------------------

The validation is based on the spring validation framework
In case of input validation Failure in the post call, the Bad request is returned along with the details containing the field name and the message

In case of invalid transactionCustomerId is send in the getTransactionTicket call, TransactionNotFoundException error will be returned with status as Not Found

---------------------------------------------------------------------------
