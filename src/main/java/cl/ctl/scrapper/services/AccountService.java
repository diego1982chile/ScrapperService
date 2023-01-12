package cl.ctl.scrapper.services;

import cl.ctl.scrapper.models.Account;
import cl.ctl.scrapper.repositories.AccountRepository;
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

    public Account getAccountByRetailerAndHolding(String retailer, String holding) {
        return accountRepository.findAccountByRetailerAndHolding(retailer, holding);
    }

    @Transactional
    public Account saveAccount(Account account) {
        if(account.isPersisted()) {
            Account previous = accountRepository.findById(account.getId());
            previous.setUser(account.getUser());
            previous.setPassword(account.getPassword());
            previous.setCompany(account.getCompany());
            previous.setRetailer(account.getRetailer());

            return accountRepository.save(previous);
        }
        else {
            return accountRepository.save(account);
        }
    }

    @Transactional
    public void deleteAccount(long id) {
        accountRepository.delete(id);
    }
}
