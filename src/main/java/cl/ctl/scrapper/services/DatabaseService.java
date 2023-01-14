package cl.ctl.scrapper.services;

import cl.ctl.scrapper.models.Account;
import cl.ctl.scrapper.models.Client;
import cl.ctl.scrapper.models.Parameter;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.repositories.AccountRepository;
import cl.ctl.scrapper.repositories.ClientRepository;
import cl.ctl.scrapper.repositories.ParameterRepository;
import cl.ctl.scrapper.repositories.RetailerRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by root on 13-10-22.
 */
@ApplicationScoped
public class DatabaseService {

    @PersistenceContext
    private EntityManager entityManager;
    private ClientRepository clientRepository;
    private RetailerRepository retailerRepository;
    private ParameterRepository parameterRepository;
    private AccountRepository accountRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.clientRepository = factory.getRepository(ClientRepository.class);
        this.retailerRepository = factory.getRepository(RetailerRepository.class);
        this.parameterRepository = factory.getRepository(ParameterRepository.class);
        this.accountRepository = factory.getRepository(AccountRepository.class);
    }

    @Transactional
    public void loadData() {
        removeAll();
        initClients();
        initRetailers();
        initParameters();
        initAccounts();
    }

    @Transactional
    private void removeAll() {
        accountRepository.removeAll();
        clientRepository.removeAll();
        retailerRepository.removeAll();
        parameterRepository.removeAll();
    }

    @Transactional
    private void initRetailers() {

        Retailer retailer = Retailer.builder().name("construmart").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("easy").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("sodimac").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("cencosud").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("smu").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("tottus").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("walmart").build();
        retailerRepository.save(retailer);

    }

    @Transactional
    private void initClients() {

        Client client = Client.builder().name("nutrisa").build();
        clientRepository.save(client);

        client = Client.builder().name("legrand").build();
        clientRepository.save(client);

        client = Client.builder().name("bless").build();
        clientRepository.save(client);

        client = Client.builder().name("soho").build();
        clientRepository.save(client);

    }

    @Transactional
    private void initParameters() {

        Parameter parameter = Parameter.builder().name("captcha.api_key").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("mail.to").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("mail.from.user").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("mail.from.password").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("error.to").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("file.download_path").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("upload.server").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("upload.host").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("upload.path").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("upload.password").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

        parameter = Parameter.builder().name("upload.target").value("[[PUT_YOUR_VALUE]]").build();
        parameterRepository.save(parameter);

    }

    @Transactional
    private void initAccounts() {

        Client bless = clientRepository.findByName("bless");
        Client legrand = clientRepository.findByName("legrand");
        Client nutrisa = clientRepository.findByName("nutrisa");
        Client soho = clientRepository.findByName("soho");

        Retailer cencosud = retailerRepository.findByName("cencosud");
        Retailer smu = retailerRepository.findByName("smu");
        Retailer tottus = retailerRepository.findByName("tottus");
        Retailer walmart = retailerRepository.findByName("walmart");
        Retailer construmart = retailerRepository.findByName("construmart");
        Retailer easy = retailerRepository.findByName("easy");
        Retailer sodimac = retailerRepository.findByName("sodimac");

        Account account = Account.builder()
                .client(bless)
                .retailer(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(bless)
                .retailer(smu)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(bless)
                .retailer(tottus)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(bless)
                .retailer(walmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(legrand)
                .retailer(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(legrand)
                .retailer(construmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(legrand)
                .retailer(easy)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(legrand)
                .retailer(sodimac)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(nutrisa)
                .retailer(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(nutrisa)
                .retailer(smu)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(nutrisa)
                .retailer(tottus)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(nutrisa)
                .retailer(walmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(soho)
                .retailer(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(soho)
                .retailer(smu)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .client(soho)
                .retailer(walmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

    }

}
