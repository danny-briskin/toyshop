package ua.com.univerpulse.toyshop.model.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.univerpulse.toyshop.model.entities.Customer;
import ua.com.univerpulse.toyshop.model.entities.projections.CustomerProjection;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RepositoryRestResource(collectionResourceRel = "customers", path = "customers"
        , excerptProjection = CustomerProjection.class)
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@CrossOrigin(origins = "https://localhost",
        methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE},
        maxAge = 3600)
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {
    @Override
    @RestResource(exported = false)
    void deleteById(@NotNull Integer id);

    @Override
    @RestResource(exported = false)
    void delete(@NotNull Customer entity);
}
