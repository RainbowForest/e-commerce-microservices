package com.rainbowforest.recommendationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "products")
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "product_name")
    private String productName;

    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<Recommendation> recomendations;
    
    public Product() {
    	
    }

    public Product(String productName, List<Recommendation> recomendations) {
        this.productName = productName;
        this.recomendations = recomendations;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Recommendation> getRecomendations() {
        return recomendations;
    }

    public void setRecomendations(List<Recommendation> recomendations) {
        this.recomendations = recomendations;
    }
}
