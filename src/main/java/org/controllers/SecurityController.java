package org.controllers;

import io.javalin.http.Handler;
import org.dtos.TokenDTO;
import org.util.TokenUtil;

public class SecurityController {


    public Handler authorization() {

        return (ctx->{
             ctx.status(200);
             return;
            //TokenDTO tokenDTO =new TokenDTO(ctx.header("username"), ctx.header("Authorization").split(" ")[1]);
            //TokenUtil.tokenIsValid(tokenDTO.getToken(),"841D8A6C80CBA4FCAD32D5367C18C53B");
            //TODO: fix when we find out how to find route roles
        });

    }
}
