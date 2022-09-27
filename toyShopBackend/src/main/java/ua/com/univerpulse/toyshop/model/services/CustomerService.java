package ua.com.univerpulse.toyshop.model.services;

import org.jetbrains.annotations.NotNull;
import ua.com.univerpulse.toyshop.exceptions.BadCustomerException;
import ua.com.univerpulse.toyshop.exceptions.CustomerNotFoundException;
import ua.com.univerpulse.toyshop.exceptions.DateParseException;
import ua.com.univerpulse.toyshop.model.dto.CustomerDto;
import ua.com.univerpulse.toyshop.model.entities.Customer;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
public interface CustomerService {
    Customer createCustomer(@NotNull CustomerDto customerDto) throws DateParseException, BadCustomerException;

    int deleteCustomer(@NotNull Integer customerId) throws CustomerNotFoundException;
}
