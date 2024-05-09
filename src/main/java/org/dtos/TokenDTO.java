package org.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {
    private String username;
    private String token;
    public TokenDTO(String username,String token){
        this.username = username;
        this.token = token;
    }

}
