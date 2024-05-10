package org.controllers;

import com.nimbusds.jose.JOSEException;
import io.javalin.http.Handler;
import org.dao.SellerDAO;
import org.dtos.SellerDTO;
import org.dtos.TokenDTO;
import org.model.Seller;
import org.util.LoggerGen;
import org.util.TokenUtil;

import java.text.ParseException;

public class SecurityController {

    private static SecurityController instance;
    public static SecurityController getInstance(){
        if(instance == null){
            instance = new SecurityController();
        }
        return instance;
    }

    public SellerDTO verifyToken(String token) throws ParseException, JOSEException {
        if(TokenUtil.tokenIsValid(token,"841D8A6C80CBA4FCAD32D5367C18C53B")){
            TokenUtil.extractFromToken(token,(jwtClaimsSet)->{
                SellerDTO seller = new SellerDTO((String) jwtClaimsSet.getClaim("username"), (String) jwtClaimsSet.getClaim("password"));

                SellerDAO sellerDAO = new SellerDAO(false);

                Seller sellerfound=  sellerDAO.getById(seller.getId());

                SellerDTO foundDTO = new SellerDTO(sellerfound);
                if(foundDTO != null){
                    return seller;
                }
                else return null;

            });

        }else{
            return null;
        }
        return null;
    }

    public Handler authorization() {

        return (ctx->{
             ctx.status(200);

        });

    }
}
