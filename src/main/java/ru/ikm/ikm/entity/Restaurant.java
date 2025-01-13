package ru.ikm.ikm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Генерация ID автоматически
    @Column(name = "id", nullable = false) //первичный ключ
    private Integer id;

    @Column(name = "name", nullable = false) //название ресторана
    private String name;

    @Column(name = "address", length = 500) //адрес ресторана
    private String address;

    @Column(name = "date_of_foundation", nullable = false) //дата основания
    private LocalDate dateOfFoundation;

    @Column(name = "phone", length = 15) //номер телефона
    private String phone;

    @Column(name = "opening_time") //время открытия
    private LocalTime openingTime;

    @Column(name = "closing_time") //время закрытия
    private LocalTime closingTime;

    @Column(name = "rating") //средний рейтинг ресторана
    private Double rating;

}