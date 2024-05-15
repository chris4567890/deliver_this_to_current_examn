package org.controllers;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.config.HibernateConfig;
import org.dao.CarDAO;
import org.dao.SellerDAO;
import org.dtos.LoginDTO;
import org.dtos.SellerDTO;
import org.dtos.TokenDTO;
import org.hibernate.cache.spi.CacheTransactionSynchronization;
import org.model.Car;
import org.model.Seller;
import org.mozilla.javascript.tools.shell.ConsoleTextArea;
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
               ctx.json("you have now registered feel free to login or don't I ain't a cop :)");
           }
           else{
               ctx.json("user already exist or you forgot password");
           }
        });
    }

    public  Handler login(){
        return(ctx ->{
            System.out.println("I am inside login for some reason");
            //System.out.println("username: " +ctx.sessionAttribute("username"));
            //System.out.println(("password: "+ctx.sessionAttribute("password")));
            LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
            System.out.println(loginDTO.getUsername());
            Seller seller = sellerDAO.getById(loginDTO.getUsername().toString());
            if(checkPass(seller,loginDTO.getPassword())){
                Map<String,String> map = new HashMap<>();
                map.put("username", loginDTO.getUsername());
                map.put("roles",seller.getRoles().toString());
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
        System.out.println("I am inside checkpassword");
        System.out.println("here is your seller password: "+seller.getPassword());
        System.out.println("here is the given pass:" +password);
        if(seller.getPassword().equals(password)){
            return true;
        }else{
            return false;
        }
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
