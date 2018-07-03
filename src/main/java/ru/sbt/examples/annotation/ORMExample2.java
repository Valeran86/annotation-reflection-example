package ru.sbt.examples.annotation;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Home on 25.06.2018.
 */
@Entity
@Builder
@Data
public class ORMExample2 {
    @Id
    private Integer id;

    @Column( nullable = false )
    private String service;

    @Column( precision = 2 )
    private Double price;
}
