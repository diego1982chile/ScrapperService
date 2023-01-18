package cl.forevision.scrapper.repositories;

import cl.forevision.scrapper.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by root on 13-10-22.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findById(long id);

    @Query("SELECT c FROM Client c where c.name = :name")
    Client findByName(@Param("name") String name);

    @Query("SELECT c FROM Client c order by c.name")
    List<Client> findAllOrderByName();

    @Modifying
    @Query("delete from Client c")
    void removeAll();
}
