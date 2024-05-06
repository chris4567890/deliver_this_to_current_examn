package org.controllers;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.config.HibernateConfig;
import org.dao.CarDAO;
import org.dao.SellerDAO;
import org.model.Car;
import org.model.Seller;

import java.util.Set;
//most of this is also rewritten from one of my group projects
public class SellerController {
    private static CarDAO carDAO;
    private static SellerDAO sellerDAO;

    public SellerController(){
        sellerDAO = SellerDAO.getInstance(false);
        carDAO = CarDAO.getInstance(false);
    }

    public Handler GetCarFromSeller() {
        return(ctx -> {
           String id = ctx.pathParam("email");
           Seller foundSeller = (Seller)sellerDAO.getCarsBySeller(id);
            Set<Car> cars = foundSeller.getCars();
            if(foundSeller != null && cars != null){
                ctx.json("Hello here is your cars: " + cars).status(HttpStatus.OK);
            }else{
                ctx.json("seller wasn't found or the seller doens't have any cars").status(HttpStatus.BAD_REQUEST);
            }

        });
    }

    public Handler addCarToSeller() {
        return(ctx ->{
           String id = ctx.pathParam("email");
           int car_id = Integer.parseInt(ctx.pathParam("car_id"));
           var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager();
           Seller foundSeller = em.find(Seller.class,id);
           Car car = carDAO.getById(car_id);
           if(foundSeller != null && car != null){
               sellerDAO.addCarToSeller(id,car_id);
           }
        });
    }
}
