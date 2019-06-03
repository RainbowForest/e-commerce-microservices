package com.rainbowforest.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "user_role")
@Data
public class UserRole {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "role_name")
    private String roleName;

    @OneToMany (mappedBy = "role")
    @JsonIgnore
    private List<User> users;
}
