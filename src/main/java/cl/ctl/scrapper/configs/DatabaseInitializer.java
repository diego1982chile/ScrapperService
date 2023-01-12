package cl.ctl.scrapper.configs;

import cl.ctl.scrapper.models.Holding;
import cl.ctl.scrapper.models.Parameter;
import cl.ctl.scrapper.models.Retailer;
import cl.ctl.scrapper.repositories.AccountRepository;
import cl.ctl.scrapper.repositories.HoldingRepository;
import cl.ctl.scrapper.repositories.ParameterRepository;
import cl.ctl.scrapper.repositories.RetailerRepository;
import lombok.extern.log4j.Log4j;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Created by root on 09-12-22.
 */
@Startup
@Singleton
@Log4j
public class DatabaseInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    private HoldingRepository holdingRepository;
    private RetailerRepository retailerRepository;
    private ParameterRepository parameterRepository;
    private AccountRepository accountRepository;

    @Resource(lookup = "java:global/scrapperDS")
    private DataSource dataSource;

    private static final String SCRIPTS_PATH = "/scripts/";

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.holdingRepository = factory.getRepository(HoldingRepository.class);
        this.retailerRepository = factory.getRepository(RetailerRepository.class);
        this.parameterRepository = factory.getRepository(ParameterRepository.class);
        this.accountRepository = factory.getRepository(AccountRepository.class);

        executeScripts();
        initHoldings();
        initRetailers();
        initParameters();

    }

    private void executeScripts() {
        try {
            if(retailerRepository.findAll().isEmpty()) {
                executeScript("retailer.sql");
                executeScript("schedule.sql");
            }
        }
        catch (Exception e) {
            executeScript("retailer.sql");
            executeScript("schedule.sql");
        }

        try {
            if(holdingRepository.findAll().isEmpty()) {
                executeScript("holding.sql");
            }
        }
        catch (Exception e) {
            executeScript("holding.sql");
        }

        try {
            if(parameterRepository.findAll().isEmpty()) {
                executeScript("parameter.sql");
            }
        }
        catch (Exception e) {
            executeScript("parameter.sql");
        }

        try {
            if(accountRepository.findAll().isEmpty()) {
                executeScript("account.sql");
            }
        }
        catch (Exception e) {
            executeScript("account.sql");
        }

    }

    @Transactional
    private void initHoldings() {
        List<Holding> holdings = holdingRepository.findAll();

        if(holdings.isEmpty()) {
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

    }

    @Transactional
    private void initRetailers() {
        List<Retailer> retailers = retailerRepository.findAll();

        if(retailers.isEmpty()) {
            Retailer retailer = Retailer.builder().name("nutrisa").build();
            retailerRepository.save(retailer);

            retailer = Retailer.builder().name("legrand").build();
            retailerRepository.save(retailer);

            retailer = Retailer.builder().name("bless").build();
            retailerRepository.save(retailer);

            retailer = Retailer.builder().name("soho").build();
            retailerRepository.save(retailer);
        }

    }

    @Transactional
    private void initParameters() {
        List<Parameter> parameters = parameterRepository.findAll();

        if(parameters.isEmpty()) {
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

    }

    private void executeScript(String script) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(SCRIPTS_PATH + script)));
        String line = "";

        String sql = "";

        try {
            while ((line = reader.readLine()) != null) {
                if(line.trim().isEmpty()) {
                    continue;
                }
                sql = sql + line;
            }
            reader.close();
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }


        try (Connection connect = dataSource.getConnection();
             Statement statement = connect.createStatement()) {

            statement.executeUpdate(sql);

            log.info("Script " + script + " ejecutado exitosamente");

        } catch (SQLException e) {
            String errorMsg = "Error al ejecutar el script " + script;
            log.error(e.getMessage());
            //throw new Exception(e.getMessage());
        }

    }

}
