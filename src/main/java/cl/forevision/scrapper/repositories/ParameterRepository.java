package cl.forevision.scrapper.repositories;


import cl.forevision.scrapper.models.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by root on 13-10-22.
 */
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    Parameter findById(long id);

    @Query("SELECT p FROM Parameter p order by p.name")
    List<Parameter> findAllOrderByName();

    @Modifying
    @Query("delete from Parameter p")
    void removeAll();

}
