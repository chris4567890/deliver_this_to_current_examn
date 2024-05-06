package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.model.Seller;

@Getter
@Setter
@NoArgsConstructor

public class SellerDTO {

    private String id;
    private String last_name;
    private String first_name;
    private int phone;
    private String city;

    public SellerDTO(Seller seller){
        this.id = seller.getEmail();
        this.last_name = seller.getLast_name();
        this.first_name = seller.getFirst_name();
        this.phone = seller.getPhone();
        this.city = seller.getCity();
    }



}
