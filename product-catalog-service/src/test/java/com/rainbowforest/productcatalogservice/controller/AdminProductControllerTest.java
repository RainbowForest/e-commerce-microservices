package com.rainbowforest.productcatalogservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rainbowforest.productcatalogservice.entity.Product;
import com.rainbowforest.productcatalogservice.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminProductControllerTest {

	private static final String PRODUCT_NAME= "test";
    private static final String PRODUCT_CATEGORY = "testCategory";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

	@Test
    public void add_product_controller_should_return201_when_product_isSaved() throws Exception {
		//given
		Product product = new Product();
		product.setProductName(PRODUCT_NAME);
		product.setCategory(PRODUCT_CATEGORY);
				
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(product);
        
        //when      
        when(productService.addProduct(new Product())).thenReturn(product);

        //then
        mockMvc.perform(post("/admin/products").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.productName").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.category").value(PRODUCT_CATEGORY));

    	verify(productService, times(1)).addProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }
	
	@Test
    public void add_product_controller_should_return400_when_product_isNull() throws Exception {
		//given
		Product product = null;			
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(product);

        //then
        mockMvc.perform(post("/admin/products").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());	
	}
}
