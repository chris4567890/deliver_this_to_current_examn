package org.config;

import io.javalin.apibuilder.EndpointGroup;
import org.controllers.CarController;
import org.controllers.SecurityController;
import org.controllers.SellerController;
import org.model.Role;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private static CarController cc;
    private static SellerController sc;
    private static SecurityController securityController ;
    public  static EndpointGroup getRoutes(boolean isTesting){
        return () ->{
            cc = new CarController();
            sc = new SellerController();
            securityController = new SecurityController();
            before(securityController.authorization());
            path("/",()->{
                get("getCars",cc.getAllCars(),Role.anyone);
                get("getCar/{id}",cc.getSpecificCar(),Role.anyone);
            });
            path("/",()->{
               post("postCar",cc.createCar(),Role.seller);
               put("updateCar/{id}",cc.updateCar(),Role.seller);
               delete("deleteCar/{id}",cc.deleteCar(),Role.seller);
            });
            path("/",()->{
               put("addCarToSeller/{email}/{car_id}",sc.addCarToSeller(),Role.admin);
               get("getCarsBySeller",sc.GetCarFromSeller(),Role.anyone);
            });


            path("/",()->{
               post("login",sc.login(), Role.anyone);
               post("register",sc.register(),Role.anyone);
            });
        } ;
    }


}
