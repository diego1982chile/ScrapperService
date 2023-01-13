package cl.ctl.scrapper.repositories;

import cl.ctl.scrapper.models.Holding;
import cl.ctl.scrapper.models.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by root on 13-10-22.
 */
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    Holding findById(long id);

    @Query("SELECT h FROM Holding h where h.name = :name")
    Holding findByName(@Param("name") String name);

    @Query("SELECT h FROM Holding h order by h.name")
    List<Holding> findAllOrderByName();

    @Modifying
    @Query("delete from Holding h")
    void removeAll();
}
