package ua.com.univerpulse.toyshop.model.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.Payment;
import ua.com.univerpulse.toyshop.model.entities.projections.PaymentProjection;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RepositoryRestResource(collectionResourceRel = "payments", path = "payments"
        , excerptProjection = PaymentProjection.class)
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@CrossOrigin(origins = "https://localhost",
        methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE},
        maxAge = 3600)
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Integer> {
    int deleteAllByCustomer(Customer customer);

}



