package cl.ctl.scrapper.services;

import cl.ctl.scrapper.models.Holding;
import cl.ctl.scrapper.models.Parameter;
import cl.ctl.scrapper.repositories.HoldingRepository;
import cl.ctl.scrapper.repositories.ParameterRepository;
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
public class ParameterService {

    @PersistenceContext
    private EntityManager entityManager;
    private ParameterRepository parameterRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.parameterRepository = factory.getRepository(ParameterRepository.class);
    }

    public List<Parameter> getAllParameters() {
        return parameterRepository.findAllOrderByName();
    }

    @Transactional
    public Parameter saveParameter(Parameter parameter) {
        if(parameter.isPersisted()) {
            Parameter previous = parameterRepository.findById(parameter.getId());
            previous.setName(parameter.getName());
            previous.setValue(parameter.getValue());

            return parameterRepository.save(previous);
        }
        else {
            return parameterRepository.save(parameter);
        }
    }

    @Transactional
    public void deleteParameter(long id) {
        parameterRepository.delete(id);
    }
}
