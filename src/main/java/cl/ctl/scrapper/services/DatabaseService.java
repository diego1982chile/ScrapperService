package cl.ctl.scrapper.services;

import cl.ctl.scrapper.models.Account;
import cl.ctl.scrapper.models.Holding;
import cl.ctl.scrapper.models.Parameter;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.repositories.AccountRepository;
import cl.ctl.scrapper.repositories.HoldingRepository;
import cl.ctl.scrapper.repositories.ParameterRepository;
import cl.ctl.scrapper.repositories.RetailerRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13-10-22.
 */
@RequestScoped
public class DatabaseService {

    @PersistenceContext
    private EntityManager entityManager;
    private HoldingRepository holdingRepository;
    private RetailerRepository retailerRepository;
    private ParameterRepository parameterRepository;
    private AccountRepository accountRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.holdingRepository = factory.getRepository(HoldingRepository.class);
        this.retailerRepository = factory.getRepository(RetailerRepository.class);
        this.parameterRepository = factory.getRepository(ParameterRepository.class);
        this.accountRepository = factory.getRepository(AccountRepository.class);
    }

    @Transactional
    public void loadData() {
        removeAll();
        initHoldings();
        initRetailers();
        initParameters();
        initAccounts();
    }

    @Transactional
    private void removeAll() {
        accountRepository.deleteAll();
        holdingRepository.deleteAll();
        retailerRepository.deleteAll();
        parameterRepository.deleteAll();
    }

    @Transactional
    private void initHoldings() {

        Holding holding = Holding.builder().name("construmart").build();
        holdingRepository.save(holding);

        holding = Holding.builder().name("easy").build();
        holdingRepository.save(holding);

        holding = Holding.builder().name("sodimac").build();
        holdingRepository.save(holding);

        holding = Holding.builder().name("cencosud").build();
        holdingRepository.save(holding);

        holding = Holding.builder().name("smu").build();
        holdingRepository.save(holding);

        holding = Holding.builder().name("tottus").build();
        holdingRepository.save(holding);

        holding = Holding.builder().name("walmart").build();
        holdingRepository.save(holding);

    }

    @Transactional
    private void initRetailers() {

        Retailer retailer = Retailer.builder().name("nutrisa").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("legrand").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("bless").build();
        retailerRepository.save(retailer);

        retailer = Retailer.builder().name("soho").build();
        retailerRepository.save(retailer);

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

        Retailer bless = retailerRepository.findByName("bless");
        Retailer legrand = retailerRepository.findByName("legrand");
        Retailer nutrisa = retailerRepository.findByName("nutrisa");
        Retailer soho = retailerRepository.findByName("soho");

        Holding cencosud = holdingRepository.findByName("cencosud");
        Holding smu = holdingRepository.findByName("smu");
        Holding tottus = holdingRepository.findByName("tottus");
        Holding walmart = holdingRepository.findByName("walmart");
        Holding construmart = holdingRepository.findByName("construmart");
        Holding easy = holdingRepository.findByName("easy");
        Holding sodimac = holdingRepository.findByName("sodimac");

        Account account = Account.builder()
                .retailer(bless)
                .holding(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(bless)
                .holding(smu)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(bless)
                .holding(tottus)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(bless)
                .holding(walmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(legrand)
                .holding(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(legrand)
                .holding(construmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(legrand)
                .holding(easy)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(legrand)
                .holding(sodimac)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(nutrisa)
                .holding(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(nutrisa)
                .holding(smu)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(nutrisa)
                .holding(tottus)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(nutrisa)
                .holding(walmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(soho)
                .holding(cencosud)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(soho)
                .holding(smu)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

        account = Account.builder()
                .retailer(soho)
                .holding(walmart)
                .user("[[PUT_YOUR_VALUE]]")
                .password("[[PUT_YOUR_VALUE]]")
                .build();

        accountRepository.save(account);

    }

}
