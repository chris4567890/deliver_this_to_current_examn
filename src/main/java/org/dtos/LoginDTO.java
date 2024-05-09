package org.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String password;
    private String username;

    public LoginDTO(String username, String password){
        this.username = username;
        this.password = password;
    }

}
