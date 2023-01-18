package cl.forevision.scrapper.repositories;

import cl.forevision.scrapper.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by root on 13-10-22.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findById(long id);

    @Query("SELECT a FROM Account a order by a.client.name, a.retailer.name")
    List<Account> findAllOrderByRetailerAndHolding();

    @Query("SELECT a FROM Account a where a.client.name = :client and a.retailer.name = :retailer")
    Account findAccountByClientAndRetailer(@Param("client") String client, @Param("retailer") String retailer);

    @Modifying
    @Query("delete from Account a")
    void removeAll();
}
