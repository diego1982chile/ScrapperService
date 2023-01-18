package cl.forevision.scrapper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * Created by root on 09-08-21.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Schedule extends AbstractPersistableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schedule;

    @ManyToOne
    @JsonBackReference
    private Retailer retailer;



}