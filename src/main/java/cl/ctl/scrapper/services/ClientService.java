package cl.ctl.scrapper.services;

import cl.ctl.scrapper.models.Account;
import cl.ctl.scrapper.models.Client;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.repositories.AccountRepository;
import cl.ctl.scrapper.repositories.ClientRepository;
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
public class ClientService {

    @PersistenceContext
    private EntityManager entityManager;
    private ClientRepository clientRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.clientRepository = factory.getRepository(ClientRepository.class);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAllOrderByName();
    }

    @Transactional
    public Client saveClient(Client client) {
        if(client.isPersisted()) {
            Client previous = clientRepository.findById(client.getId());
            previous.setName(client.getName());
            /*
            if(retailer.getSchedules() != null) {
                previous.setSchedules(retailer.getSchedules());
            }
            */

            return clientRepository.save(previous);
        }
        else {
            return clientRepository.save(client);
        }
    }

    @Transactional
    public void deleteClient(long id) {
        clientRepository.delete(id);
    }
}
