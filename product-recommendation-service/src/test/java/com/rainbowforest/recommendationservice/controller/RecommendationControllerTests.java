package com.rainbowforest.recommendationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rainbowforest.recommendationservice.feignClient.ProductClient;
import com.rainbowforest.recommendationservice.feignClient.UserClient;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;
    
    @MockBean
    private ProductClient productClient;
    
    @MockBean
    private UserClient userClient;

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
    public void get_all_rating_controller_should_return200_when_validRequest() throws Exception {
    	//when
        when(recommendationService.getAllRecommendationByProductName(anyString())).thenReturn(recommendations);

        //then
        mockMvc.perform(get("/recommendations").param("name", PRODUCT_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").value(RECOMMENDATION_ID))
                .andExpect(jsonPath("$[0].rating").value(RATING))
                .andExpect(jsonPath("$[0].product.productName").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[0].user.userName").value(USER_NAME));
              
        verify(recommendationService, times(1)).getAllRecommendationByProductName(anyString());
        verifyNoMoreInteractions(recommendationService);
    }
    
    @Test
    public void get_all_rating_controller_should_return404_when_recommendationList_isEmpty() throws Exception {
    	//given
    	List<Recommendation> recommendations = new ArrayList<Recommendation>();
    	
    	//when
    	when(recommendationService.getAllRecommendationByProductName(anyString())).thenReturn(recommendations);

        //then
        mockMvc.perform(get("/recommendations").param("name", PRODUCT_NAME))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE));
                          
        verify(recommendationService, times(1)).getAllRecommendationByProductName(anyString());
        verifyNoMoreInteractions(recommendationService);
    }

    @Test
    public void save_recommendations_controller_should_return201_when_user_is_saved() throws Exception {
    	//given
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(recommendation);
        
        //when
        when(productClient.getProductById(PRODUCT_ID)).thenReturn(product);
        when(userClient.getUserById(USER_ID)).thenReturn(user);
        when(recommendationService.saveRecommendation(any(Recommendation.class))).thenReturn(recommendation);
        
        //then
        mockMvc.perform(post("/{userId}/recommendations/{productId}",USER_ID, PRODUCT_ID).param("rating", RATING.toString()).content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.rating").value(RATING));

        verify(recommendationService, times(1)).saveRecommendation(any(Recommendation.class));
        verifyNoMoreInteractions(recommendationService);
    }
}
