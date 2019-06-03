package com.rainbowforest.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table (name = "users_details")
@Data
public class UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "first_name")
    private String firstName;
    @Column (name = "last_name")
    private String lastName;
    @Column (name = "email")
    private String email;
    @Column (name = "phone_number")
    private String phoneNumber;
    @Column (name = "street")
    private String street;
    @Column (name = "street_number")
    private String streetNumber;
    @Column (name = "zip_code")
    private String zipCode;
    @Column (name = "locality")
    private String locality;
    @Column (name = "country")
    private String country;

    @OneToOne(mappedBy = "userDetails")
    @JsonIgnore
    private User user;
}
