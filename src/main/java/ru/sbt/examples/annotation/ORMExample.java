package ru.sbt.examples.annotation;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by Home on 25.06.2018.
 */
@Entity
@Builder
@Data
public class ORMExample {
    @Id
    @GeneratedValue
    private Integer id;
    @Column( length = 30, name = "NAME_IN_TABLE" )
    private String name;
    @Column( nullable = false )
    private Boolean active;
}
