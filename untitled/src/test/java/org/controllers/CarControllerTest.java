package org.controllers;

import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.config.ApplicationConfig;
import org.config.HibernateConfig;
import org.config.Routes;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.model.Car;

import static org.junit.jupiter.api.Assertions.*;
// this is also  from one of my group projects which

class CarControllerTest {
    private static EntityManagerFactory emf;
    @BeforeAll
    static void preSetup(){
        emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();
        RestAssured.baseURI = "http://localhost:6969/api";
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(6969)
                .setExceptionHandling()
                .setRoutes(Routes.getRoutes(true));
        Car testCar1 = new Car(0,"Tesla","elon","musk","today",2024,300);
        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()){
            em.getTransaction().begin();
            em.persist(testCar1);
            em.getTransaction().commit();
        }

    }
    @BeforeEach
    void setUp(){
        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("delete from Car ").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE Cars RESTART WITH 1");
        }
    }
    @AfterAll
    static void remove(){
        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("delete from Car ");
            em.getTransaction().commit();
        }
    }
}