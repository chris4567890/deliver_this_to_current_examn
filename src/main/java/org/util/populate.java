package org.util;

import org.config.HibernateConfig;
import org.model.Car;
import org.model.Role;
import org.model.Seller;

import java.util.HashSet;
import java.util.Set;

public class populate {
    Set<Car> bobsSet =  new HashSet<>();

    Set<Car> meSet = new HashSet<>();
    Car testCar1 = new Car(0,"test","test","test","test",1999,50);
    Car testCar2 = new Car(1,"test","test","test","test",1999,50);
    Car testCar3 = new Car(2,"test","test","test","test",1999,50);
    Car testCar4 = new Car(3,"test","test","test","test",1999,50);
    Car testCar5 = new Car(4,"test","test","test","test",1999,50);
    public void fillSet(Car car){
        bobsSet.add(car);
    }

    Seller seller1 = new Seller("bob@builder.com","bob","builder","1243",11111,"københaven",bobsSet,Set.of(Role.seller));
    Seller seller2 = new Seller("ours@ours.com","me","the awesome","1243",4949,"lyngby",meSet,Set.of(Role.admin,Role.seller) );

    public void popDb(){
        fillSet(testCar1);
        fillSet(testCar2);
        fillSet(testCar3);
        fillSet(testCar4);
        fillSet(testCar5);
        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()){
            em.getTransaction().begin();
            em.persist(testCar1);
            em.persist(testCar2);
            em.persist(testCar3);
            em  .persist(testCar4);
            em.persist(testCar5);
            em.persist(seller1);
            em.getTransaction().commit();
            em.close();
        }
    }

}
