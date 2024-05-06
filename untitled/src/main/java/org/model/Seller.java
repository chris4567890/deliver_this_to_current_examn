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
    String email;
    String first_name;
    String last_name;
    int phone;

    String city;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Car> cars = new HashSet<>();

}
