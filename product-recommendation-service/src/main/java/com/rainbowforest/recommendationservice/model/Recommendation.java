package com.rainbowforest.recommendationservice.model;

import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table (name = "recommendation")
public class Recommendation {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "rating")
    private int rating;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "product_id")

    private Product product;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    private User user;
    
    public Recommendation() {
	
	}

	public Recommendation(int rating, Product product, User user) {
        this.rating = rating;
        this.product = product;
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
