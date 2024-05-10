package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.model.Role;
import org.model.Seller;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

public class SellerDTO {

    private String id;
    private String last_name;
    private String first_name;
    private String password;
    private int phone;
    private String city;
    private Set<Role> roles = new HashSet<>();;

    //this is a comment so I can push the shit.
    public SellerDTO(Seller seller){
        this.id = seller.getEmail();
        this.last_name = seller.getLast_name();
        this.first_name = seller.getFirst_name();
        this.phone = seller.getPhone();
        this.city = seller.getCity();
        this.password = seller.getPassword();
    }

    public SellerDTO(String id,String password){
        this.id = id;
        this.password = password;
    }

}
