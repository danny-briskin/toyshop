package ua.com.univerpulse.toyshop.model.services;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.univerpulse.toyshop.exceptions.BadCustomerException;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.repositories.CustomerRepository;
import ua.com.univerpulse.toyshop.model.repositories.PaymentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Service(value = "customerService")
@Log4j2
public class CustomerServiceImpl extends AbstractService implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Contract(pure = true)
    public CustomerServiceImpl(@Autowired CustomerRepository customerRepository
            , @Autowired PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    private void checkCustomerDto(@NotNull CustomerDto customerDto) throws BadCustomerException {
        if (Strings.isEmpty(customerDto.getCustomerName())) {
            throw new BadCustomerException("Customer's name field [customerName] should not be empty.");
        }
        if (customerDto.getCustomerBalance() == null) {
            throw new BadCustomerException("Customer's balance field [customerBalance] should not be empty.");
        }
        if (Strings.isEmpty(customerDto.getBillingAddress())) {
            throw new BadCustomerException("Customer's billing address field [billingAddress] should not be empty.");
        }
    }

    @Override
    @Transactional
    public Customer createCustomer(@NotNull CustomerDto customerDto)
            throws DateParseException, BadCustomerException {
        this.checkCustomerDto(customerDto);
        Customer customer = new Customer();
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setBillingAddress(customerDto.getBillingAddress());
        customer.setCustomerBalance(customerDto.getCustomerBalance());
        LocalDateTime customerActivated;
        LocalDateTime customerDeactivated;
        if (customerDto.getCustomerActivated() == null) {
            customerActivated = LocalDateTime.now();
        } else {
            customerActivated = super.tryToParseStringDate(customerDto.
                    getCustomerActivated());
        }
        if (customerDto.getCustomerDeactivated() == null) {
            customerDeactivated = null;
        } else {
            customerDeactivated = super.tryToParseStringDate(customerDto.
                    getCustomerDeactivated());
        }
        customer.setCustomerActivated(customerActivated);
        customer.setCustomerDeactivated(customerDeactivated);

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public int deleteCustomer(@NotNull Integer customerId) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            log.debug("Deleted " +
                    paymentRepository.deleteAllByCustomer(customer.get())
                    + " payments");
            customerRepository.delete(customer.get());
            log.debug("Deleted " + customerId + " customerID");
            return customerId;
        } else {
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
    }
}
