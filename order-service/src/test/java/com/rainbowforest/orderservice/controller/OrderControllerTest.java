package com.rainbowforest.orderservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import com.rainbowforest.orderservice.domain.Item;
import com.rainbowforest.orderservice.domain.Order;
import com.rainbowforest.orderservice.domain.Product;
import com.rainbowforest.orderservice.domain.User;
import com.rainbowforest.orderservice.feignclient.UserClient;
import com.rainbowforest.orderservice.service.CartService;
import com.rainbowforest.orderservice.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    private static final String PRODUCT_NAME= "test";
    private static final Long PRODUCT_ID = 5L;
    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "Test";
    private static final String CART_ID = "1";
    Cookie mockCookie = Mockito.mock(Cookie.class);
	
    @Autowired
    private MockMvc mockMvc;
    
	@MockBean
	private UserClient userClient;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private CartService cartService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before()
	public void setup()
	{
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void save_order_controller_should_return201_when_valid_request() throws Exception {
		//given
		Product product = new Product();
		product.setId(PRODUCT_ID);
		product.setProductName(PRODUCT_NAME);
		
		User user = new User();
		user.setUserName(USER_NAME);
				
		Item item = new Item();
		item.setProduct(product);
		item.setQuantity(1);
		List<Item> cart = new ArrayList<Item>();
		cart.add(item);
		
		Order order = new Order();
		order.setItems(cart);
		order.setUser(user);
		
		Cookie cookie = new Cookie("cartId", CART_ID);
		
		//when
		when(cartService.getAllItemsFromCart(CART_ID)).thenReturn(cart);
		when(userClient.getUserById(USER_ID)).thenReturn(user);
		when(orderService.saveOrder(new Order())).thenReturn(order);
		//then
		
		mockMvc.perform(post("/order/{userId}", USER_ID).cookie(new Cookie[] {cookie}))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.items").isArray());

		verify(orderService, times(1)).saveOrder(any(Order.class));
		verifyNoMoreInteractions(orderService);
	}
}
