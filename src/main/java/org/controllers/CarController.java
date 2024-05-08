package org.controllers;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.config.HibernateConfig;
import org.dao.CarDAO;
import org.dtos.CarDTO;
import org.model.Car;
import org.util.LoggerGen;

import java.util.ArrayList;
import java.util.List;
//most of this is also rewritten from one of my group projects
public class CarController {

    private static CarDAO carDAO;
    private static LoggerGen loggerGen;
    public CarController(){
        carDAO = CarDAO.getInstance(false);
        loggerGen = LoggerGen.getInstance();
    }

    public Handler getAllCars() {
        return(ctx -> {
           var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager();
            List<Car> cars = (List<Car>) em.createQuery("select c from Car c").getResultList();
            List<CarDTO> carDTOS = new ArrayList<>();
            for(Car c : cars){
                CarDTO carDTO = new CarDTO(c);
                carDTOS.add(carDTO);
            }
            ctx.json(carDTOS).status(HttpStatus.OK);
            loggerGen.logToConsole("got all cars");
        });
    }

    public Handler getSpecificCar() {
        return(ctx ->{
           int id = Integer.parseInt((ctx.pathParam("id")));
           Car car = (Car) carDAO.getById(id);
           CarDTO carDTO = new CarDTO(car);
           if(car != null){
               ctx.json(carDTO);
               loggerGen.logToConsole("got specific car: "+carDTO);
           }

        });
    }

    public Handler createCar() {
        return (ctx->{
           Car car = ctx.bodyAsClass(Car.class);
           carDAO.create(car);
           CarDTO carDTO = new CarDTO(car);
           ctx.json(carDTO).status(HttpStatus.CREATED);
           loggerGen.logToConsole("created car:" + carDTO);
        });
    }

    public Handler updateCar() {
        return(ctx -> {
           CarDTO carDTO = ctx.bodyAsClass(CarDTO.class);
           int id = Integer.parseInt(ctx.pathParam("id"));
           Car foundCar = carDAO.getById(id);
           var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager();
           em.getTransaction().begin();
           if(foundCar != null){
               foundCar.setId(carDTO.getId());
               foundCar.setBrand(carDTO.getBrand());
               foundCar.setMake(carDTO.getMake());
               foundCar.setModel(carDTO.getModel());
               foundCar.setTime_registration(carDTO.getTime_registration());
               foundCar.setPrice(carDTO.getPrice());
               //carDAO.update(foundCar, foundCar.getId());
               ctx.json(carDTO).status(HttpStatus.OK);
               loggerGen.logToConsole("updated car :" + carDTO);
           }
           else{
               ctx.json("car not found").status(HttpStatus.BAD_REQUEST);
           }
        });
    }

    public Handler deleteCar() {
        return ctx -> {
          int id = Integer.parseInt(ctx.pathParam("id"));
          Car foundCar = carDAO.getById(id);
          /*var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager();
          em.getTransaction().begin();*/
          if(foundCar != null){
              //em.remove(foundCar);
              //em.getTransaction().commit();
              carDAO.delete(id);
              ctx.json(foundCar).status(HttpStatus.OK);
              loggerGen.logToConsole("deleted the car : " + carDAO);
          }
          else{
              ctx.json("car not found").status(HttpStatus.BAD_REQUEST);
              loggerGen.logToConsole("couldn't delete car");
          }
        };
    }
}
