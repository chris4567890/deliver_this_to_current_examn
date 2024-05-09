package org.config;

import io.javalin.apibuilder.EndpointGroup;
import org.controllers.CarController;
import org.controllers.SellerController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private static CarController cc;
    private static SellerController sc;
    public  static EndpointGroup getRoutes(boolean isTesting){
        return () ->{
            cc = new CarController();
            sc = new SellerController();
            path("/",()->{
                get("getCars",cc.getAllCars());
                get("getCar/{id}",cc.getSpecificCar());
            });
            path("/",()->{
               post("postCar",cc.createCar());
               put("updateCar/{id}",cc.updateCar());
               delete("deleteCar/{id}",cc.deleteCar());
            });
            path("/",()->{
               put("addCarToSeller/{email}/{car_id}",sc.addCarToSeller());
               get("getCarsBySeller",sc.GetCarFromSeller());
            });

            path("/",()->{
               post("login",sc.login());
               post("register",sc.register());
            });
        } ;
    }


}
