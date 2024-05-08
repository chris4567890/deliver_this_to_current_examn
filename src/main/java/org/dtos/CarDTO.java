package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.model.Car;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class CarDTO {

    private int id;
    private String brand;
    private String model;
    private String make;


    private String time_registration;
    private int year;

    private double price;

    public CarDTO(int id, String model, String make, String reigstration_date,int year ,double price){
        this.id = id;
        this.model = model;
        this.make = make;

        this.time_registration = reigstration_date;
        this.price = price;
        this.year = year;
    }
    public CarDTO(Car car){
        this.id = car.getId();
        this.model = car.getModel();
        this.make = car.getMake();
        this.time_registration = car.getTime_registration();
        this.year = car.getYear();
    }

}
