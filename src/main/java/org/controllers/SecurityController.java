package org.controllers;

import io.javalin.http.Handler;
import org.dtos.TokenDTO;
import org.util.TokenUtil;

public class SecurityController {


    public Handler authorization() {

        return (ctx->{
            TokenDTO tokenDTO =new TokenDTO(ctx.header("username"), ctx.header("Authorization").split(" ")[1]);
            TokenUtil.tokenIsValid(tokenDTO.getToken(),"841D8A6C80CBA4FCAD32D5367C18C53B");

        });

    }
}
