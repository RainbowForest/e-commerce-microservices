package com.rainbowforest.recommendationservice.service;

import com.rainbowforest.recommendationservice.model.Recommendation;
import java.util.List;

public interface RecommendationService {
    public Recommendation saveRecommendation(Recommendation recommendation);
    public List<Recommendation> getAllRecommendationByProductName(String productName);
    public void deleteRecommendation(Long id);
}
