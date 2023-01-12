package cl.ctl.scrapper.services;

import cl.ctl.scrapper.models.Holding;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.repositories.HoldingRepository;
import cl.ctl.scrapper.repositories.RetailerRepository;
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
public class HoldingService {

    @PersistenceContext
    private EntityManager entityManager;
    private HoldingRepository holdingRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.holdingRepository = factory.getRepository(HoldingRepository.class);
    }

    public List<Holding> getAllRetailers() {
        return holdingRepository.findAllOrderByName();
    }

    @Transactional
    public Holding saveHolding(Holding holding) {
        if(holding.isPersisted()) {
            Holding previous = holdingRepository.findById(holding.getId());
            previous.setName(holding.getName());

            return holdingRepository.save(previous);
        }
        else {
            return holdingRepository.save(holding);
        }
    }

    @Transactional
    public void deleteHolding(long id) {
        holdingRepository.delete(id);
    }
}
