package org.controllers;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.config.HibernateConfig;
import org.dao.CarDAO;
import org.dao.SellerDAO;
import org.dtos.LoginDTO;
import org.dtos.SellerDTO;
import org.dtos.TokenDTO;
import org.model.Car;
import org.model.Seller;
import org.util.TokenUtil;

import java.util.HashMap;
import java.util.Map;
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

    public Handler register(){
        return(ctx -> {
           LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
           Seller seller = sellerDAO.getById(loginDTO.getUsername());
           if(seller == null && loginDTO.getPassword() != null){
               sellerDAO.create(seller);
               login();
           }
           else{
               ctx.json("user already exist or you forgot password");
           }
        });
    }

    public  Handler login(){
        return(ctx ->{
            LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
            Seller seller = sellerDAO.getById(loginDTO.getUsername());
            if(checkPass(seller,loginDTO.getPassword())){
                Map<String,String> map = new HashMap<>();
                map.put("username", loginDTO.getUsername());
                map.put("roles","seller");
                String token = TokenUtil.createToken("cphbuisness",
                        1800000,
                        "841D8A6C80CBA4FCAD32D5367C18C53B",
                        "login",
                        map );
                TokenDTO tokenDTO = new TokenDTO(loginDTO.getUsername(),token);

                ctx.header("Authorization","Bearer "+tokenDTO.getToken());
                ctx.header("username"+tokenDTO.getUsername());
                ctx.json(tokenDTO);
            }
        });
    }

    private boolean checkPass(Seller seller,String password){
        return seller.getPassword() == password;
        //TODO: change to salt later
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
