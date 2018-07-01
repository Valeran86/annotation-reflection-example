package ru.sbt.examples.annotation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * II. Аннотации
 * Проверить экземпляр класса на соответствие ограничениям, заданным в аннотации:
 * <p>
 * 1. Если есть колонка помеченная как @Id, то либо значение поля не null, либо также есть аннотация @GeneratedValue
 *
 * @see javax.persistence.Id
 * @see javax.persistence.GeneratedValue
 * <p>
 * 2. Проверить что значение в колонке, отмеченной аннотацией @Column соответсвует заданным ограничениям
 * @see javax.persistence.Column
 */
public class AnnotationExample {

    public static void main( String... args ) {
//Задача закрыта. Убрал чтобы не отвлекало.
    }

}
