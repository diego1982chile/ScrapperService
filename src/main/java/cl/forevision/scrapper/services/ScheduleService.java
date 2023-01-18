package cl.forevision.scrapper.services;

import cl.forevision.scrapper.models.Schedule;
import cl.forevision.scrapper.repositories.RetailerRepository;
import cl.forevision.scrapper.repositories.ScheduleRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by root on 13-10-22.
 */
@RequestScoped
public class ScheduleService {

    @PersistenceContext
    private EntityManager entityManager;
    private ScheduleRepository scheduleRepository;
    private RetailerRepository retailerRepository;

    @PostConstruct
    private void init() {
        // Instantiate Spring Data factory
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        // Get an implemetation of PersonRepository from factory
        this.scheduleRepository = factory.getRepository(ScheduleRepository.class);
        this.retailerRepository = factory.getRepository(RetailerRepository.class);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAllOrderByName();
    }

    public List<Schedule> getSchedulesByRetailer(String retailer) {
        return scheduleRepository.findByRetailer(retailer);
    }

    @Transactional
    public Schedule saveSchedule(Schedule schedule) {
        if(schedule.isPersisted()) {
            Schedule previous = scheduleRepository.findById(schedule.getId());
            previous.setRetailer(schedule.getRetailer());
            previous.setSchedule(schedule.getSchedule());

            return scheduleRepository.save(previous);
        }
        else {
            //Retailer retailer = retailerRepository.findById(schedule.getRetailer().getId());
            //retailer.getSchedules().add(schedule);
            return scheduleRepository.save(schedule);
        }
    }

    @Transactional
    public void deleteParameter(long id) {
        scheduleRepository.delete(id);
    }
}
