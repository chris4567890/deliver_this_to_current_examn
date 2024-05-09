package org.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    int phone;
    //random comment to make it work
    String city;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Car> cars = new HashSet<>();
    Set<Role> roles = new HashSet<>();



}
