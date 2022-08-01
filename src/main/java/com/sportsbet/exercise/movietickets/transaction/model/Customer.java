package com.sportsbet.exercise.movietickets.transaction.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Model object for Customer
 */
@Getter @Setter @ToString
public class Customer {

    @NotBlank
    private String name;

    @Min(0) @Max(150) @NotNull
    private Integer age;

    public Customer(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
