package com.rainbowforest.recommendationservice.controller;

import com.rainbowforest.recommendationservice.feignClient.ProductClient;
import com.rainbowforest.recommendationservice.feignClient.UserClient;
import com.rainbowforest.recommendationservice.model.Product;
import com.rainbowforest.recommendationservice.model.Recommendation;
import com.rainbowforest.recommendationservice.model.User;
import com.rainbowforest.recommendationservice.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @GetMapping(value = "/recommendations")
    private List<Recommendation> getAllRating(@RequestParam("name") String productName){
        return recommendationService.getAllRecommendationByProductName(productName);
    }

    @PostMapping(value = "/{userId}/recommendations/{productId}")
    private ResponseEntity<Recommendation> saveRecommendations(
            @PathVariable ("userId") Long userId,
            @PathVariable ("productId") Long productId,
            @RequestParam ("rating") int rating,
            HttpServletRequest request) throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        Recommendation recommendation = new Recommendation();
        Product product = productClient.getProductById(productId);
        User user = userClient.getOneUser(userId);
        recommendation.setProduct(product);
        recommendation.setUser(user);
        recommendation.setRating(rating);
        recommendationService.saveRecommendation(recommendation);
        headers.setLocation(new URI(request.getRequestURI() + "/" + recommendation.getId()));
        return new ResponseEntity<Recommendation>(recommendation, headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/recommendations/{id}")
    private ResponseEntity deleteRecommendations(@PathVariable("id") Long id){
        recommendationService.deleteRecommendation(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
