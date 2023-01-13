package cl.ctl.scrapper.repositories;

import cl.ctl.scrapper.models.Account;
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

    @Query("SELECT a FROM Account a order by a.retailer.name, a.holding.name")
    List<Account> findAllOrderByRetailerAndHolding();

    @Query("SELECT a FROM Account a where a.retailer.name = :retailer and a.holding.name = :holding")
    Account findAccountByRetailerAndHolding(@Param("retailer") String retailer, @Param("holding") String holding);

    @Modifying
    @Query("delete from Account a")
    void removeAll();
}
