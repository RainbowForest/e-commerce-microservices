package com.rainbowforest.recommendationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainbowforest.recommendationservice.model.Product;
import com.rainbowforest.recommendationservice.model.Recommendation;
import com.rainbowforest.recommendationservice.model.User;
import com.rainbowforest.recommendationservice.service.RecommendationService;

import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(properties = {

})
public class RecommendationControllerTests {

    private final Long PRODUCT_ID = 1L;
    private final Long USER_ID = 1L;
    private final Long RECOMMENDATION_ID = 1L;
    private final Integer RATING = 5;
    private final String PRODUCT_NAME = "testProduct";
    private final String USER_NAME = "testUser";
    private User user;
    private Product product;
    private Recommendation recommendation;
    private List<Recommendation> recommendations;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;



    @Before
    public void setUp(){
        user = new User();
        user.setUserName(USER_NAME);
        product = new Product();
        product.setProductName(PRODUCT_NAME);
        recommendation = new Recommendation();
        recommendation.setId(RECOMMENDATION_ID);
        recommendation.setUser(user);
        recommendation.setProduct(product);
        recommendation.setRating(RATING);
        recommendations = new ArrayList<>();
        recommendations.add(recommendation);
    }

    @Test
    public void get_all_rating_controller_test() throws Exception {
        when(recommendationService.getAllRecommendationByProductName(anyString())).thenReturn(recommendations);

        mockMvc.perform(get("/recommendations").param("name", PRODUCT_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").value(RECOMMENDATION_ID))
                .andExpect(jsonPath("$[0].rating").value(RATING))
                .andExpect(jsonPath("$[0].product.productName").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[0].user.userName").value(USER_NAME))
                .andDo(print());

        verify(recommendationService, times(1)).getAllRecommendationByProductName(anyString());
        verifyNoMoreInteractions(recommendationService);
    }

    @Test
    public void save_recommendations_controller_test() throws Exception {
        String recommendationJson = objectMapper.writeValueAsString(recommendation);
        when(recommendationService.saveRecommendation(any(Recommendation.class))).thenReturn(recommendation);
        mockMvc.perform(post("/{userId}/recommendations/{productId}",USER_ID, PRODUCT_ID).param("rating", RATING.toString()).content(recommendationJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.rating").value(RATING))
                .andDo(print());

        verify(recommendationService, times(1)).saveRecommendation(any(Recommendation.class));
        verifyNoMoreInteractions(recommendationService);
    }

}
