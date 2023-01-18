package cl.forevision.scrapper.services;

import cl.forevision.scrapper.models.Account;
import cl.forevision.scrapper.repositories.AccountRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 13-10-22.
 */
@RequestScoped
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;
    private AccountRepository accountRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.accountRepository = factory.getRepository(AccountRepository.class);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAllOrderByRetailerAndHolding();
    }

    public Account getAccountByClientAndRetailer(String client, String retailer) {
        return accountRepository.findAccountByClientAndRetailer(client, retailer);
    }

    @Transactional
    public Account saveAccount(Account account) throws SQLException {
        if(account.isPersisted()) {
            Account previous = accountRepository.findById(account.getId());
            previous.setUsername(account.getUsername());
            previous.setPassword(account.getPassword());
            previous.setCompany(account.getCompany());
            previous.setRetailer(account.getRetailer());

            return accountRepository.save(previous);
        }
        else {
            if(getAccountByClientAndRetailer(account.getClient().getName(), account.getRetailer().getName()) != null) {
                throw new SQLException("A UNIQUE constraint failed (UNIQUE constraint failed: ACCOUNT.CLIENT_ID, ACCOUNT.RETAILER_ID)");
            }
            return accountRepository.save(account);
        }
    }

    @Transactional
    public void deleteAccount(long id) {
        accountRepository.delete(id);
    }
}
