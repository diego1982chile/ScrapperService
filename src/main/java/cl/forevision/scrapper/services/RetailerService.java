package cl.forevision.scrapper.services;

import cl.forevision.scrapper.models.Retailer;
import cl.forevision.scrapper.repositories.RetailerRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by root on 13-10-22.
 */
@RequestScoped
public class RetailerService {

    @PersistenceContext
    private EntityManager entityManager;
    private RetailerRepository retailerRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.retailerRepository = factory.getRepository(RetailerRepository.class);
    }

    public List<Retailer> getAllRetailers() {
        return retailerRepository.findAllOrderByName();
    }

    @Transactional
    public Retailer saveRetailer(Retailer retailer) {
        if(retailer.isPersisted()) {
            Retailer previous = retailerRepository.findById(retailer.getId());
            previous.setName(retailer.getName());

            return retailerRepository.save(previous);
        }
        else {
            return retailerRepository.save(retailer);
        }
    }

    @Transactional
    public void deleteRetailer(long id) {
        retailerRepository.delete(id);
    }
}
