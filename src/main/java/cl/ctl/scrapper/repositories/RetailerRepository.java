package cl.ctl.scrapper.repositories;

import cl.ctl.scrapper.models.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by root on 13-10-22.
 */
public interface RetailerRepository extends JpaRepository<Retailer, Long> {

    Retailer findById(long id);

    @Query("SELECT r FROM Retailer r where r.name = :name")
    Retailer findByName(@Param("name") String name);

    @Query("SELECT r FROM Retailer r order by r.name")
    List<Retailer> findAllOrderByName();

    @Modifying
    @Query("delete from Retailer h")
    void removeAll();
}
