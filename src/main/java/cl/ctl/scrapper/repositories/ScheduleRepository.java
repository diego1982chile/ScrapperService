package cl.ctl.scrapper.repositories;

import cl.ctl.scrapper.models.Parameter;
import cl.ctl.scrapper.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by root on 13-10-22.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule findById(long id);

    @Query("SELECT s FROM Schedule s order by s.retailer.name, s.schedule")
    List<Schedule> findAllOrderByName();

    @Query("SELECT s FROM Schedule s where s.retailer.name = :retailer  order by s.retailer.name, s.schedule")
    List<Schedule> findByRetailer(@Param("retailer") String retailer);

    @Modifying
    @Query("delete from Schedule s")
    void removeAll();

}
