package ua.com.univerpulse.toyshop.controllers;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.com.univerpulse.toyshop.exceptions.BadCustomerException;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.exceptions.PaymentNotFoundException;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.dto.PaymentDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.services.CustomerService;
import ua.com.univerpulse.toyshop.model.services.PaymentService;


/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RestController
@Log4j2
@CrossOrigin(origins = "https://localhost", maxAge = 3600)
public class AppController {
    private final PaymentService paymentService;
    private final CustomerService customerService;

    private static final String NOT_FOUND = "] was not found";
    private static final String CUSTOMER = "Customer with ID [";
    private static final String DELETED = "] was deleted";

    @Contract(pure = true)
    public AppController(@Autowired PaymentService paymentService
            , @Autowired CustomerService customerService) {
        this.paymentService = paymentService;
        this.customerService = customerService;
    }

    /**
     * 2.1 Create payment
     *
     * @param paymentDto json like  {
     *                   "customerId":68
     *                   ,"paymentAmount":23.45
     *                   ,"channel" : "PostMan 68"
     *                   }
     * @return ResponseEntity PaymentDto
     */
    @PostMapping(value = "/api/createPayment", headers = {"Accept=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            Payment payment = paymentService.makePayment(paymentDto);
            return new ResponseEntity<>(new PaymentDto(payment), HttpStatus.CREATED);
        } catch (CustomerNotFoundException customerNotFoundException) {
            log.catching(customerNotFoundException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(paymentDto);
        }
    }

    /**
     * 3.1 Update payment by id
     *
     * @param id         payment ID
     * @param paymentDto data to update. json like  {
     *                   "customerId":2
     *                   ,"paymentAmount":45.67
     *                   ,"channel" : "PostMan A"
     *                   }
     * @return ResponseEntity
     */
    @PutMapping(value = "/api/updatePayment/{id}", headers = {"Accept=application/json"})
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable("id") Integer id
            , @RequestBody PaymentDto paymentDto) {
        try {
            Payment newPayment = paymentService.updatePayment(id, paymentDto);
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentDto(newPayment));
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer of payment ID [" + id + NOT_FOUND, e);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment ID [" + id + NOT_FOUND, e);
        } catch (DateParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED, "Payment Date [" + paymentDto.getPaymentDate()
                    + " format is not supported", e);
        }
    }

    /**
     * 4.1 Delete payment by id
     *
     * @param id id of payment
     * @return "Payment " + id + " deleted"
     */
    @DeleteMapping(value = "/api/deletePayment/{id}")
    public ResponseEntity<String> deletePaymentById(@PathVariable("id") Integer id) {
        try {
            paymentService.deletePayment(id);
            return new ResponseEntity<>("{\"paymentId\": " + id
                    + ",\"status\":\"deleted\"}", HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment ID [" + id + NOT_FOUND, e);
        }
    }

    /**
     * 5.1 Create customer
     *
     * @param customerDto json like {
     *                    "customerName": "A1A",
     *                    "billingAddress": "billingAddress 1312",
     *                    "customerBalance": 123
     *                    }
     *                    Optional fields: :
     *                    "customerActivated":"2010-01-02" -
     *                    otherwise current date
     *                    "customerDeactivated":"2010-01-02"
     *                    - otherwise null
     * @return customer
     * @see CustomerDto
     */
    @PostMapping(value = "/api/createCustomer"
            , headers = {"Accept=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            Customer customer = customerService.createCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CustomerDto(customer));
        } catch (DateParseException | BadCustomerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * 5.2 Delete customer
     *
     * @param customerId customerId
     * @return customerId
     */
    @DeleteMapping(value = "/api/deleteCustomer/{customerId}")
    @ResponseBody
    public ResponseEntity<String> deleteCustomer
    (@PathVariable("customerId") Integer customerId) {
        try {
            log.debug(CUSTOMER + customerService.deleteCustomer(customerId) + DELETED);
            return ResponseEntity.status(HttpStatus.OK).body(CUSTOMER
                    + customerId + DELETED);
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, CUSTOMER + customerId + NOT_FOUND, e);
        }
    }
}
