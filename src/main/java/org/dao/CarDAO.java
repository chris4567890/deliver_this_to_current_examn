package org.dao;

import org.config.HibernateConfig;
import org.model.Car;
import org.model.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CarDAO extends DAO<Car,Integer> {

    private static CarDAO instance;

    public static CarDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new CarDAO(isTesting);
        }
        return instance;
    }
    public CarDAO(boolean isTesting){
       super(Car.class,isTesting);
    }
    @Override
    public Car getById(Integer id){
        Car car;
        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()){
            em.getTransaction().begin();
            car = em.find(Car.class,id);

        }
        return car;
    }


    public void addCarToSeller(String seller_id, int id){
        Seller seller;
        Car car;
        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()) {
            em.getTransaction().begin();
            car = em.find(Car.class,id);
            seller = em.find(Seller.class,seller_id);
            Set<Car> cars = seller.getCars();
            if(car != null && seller != null){
                cars.add(car);
            }
        }
    }

}
