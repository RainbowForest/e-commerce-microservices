package com.rainbowforest.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rainbowforest.userservice.entity.User;
import com.rainbowforest.userservice.service.UserService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    private final Long USER_ID = 2L;
    private final String USER_NAME = "test";
    private User user;
    private List<User> users;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;
    
    @Test
	public void get_all_users_controller_should_return200_when_validRequest() throws Exception{	
    	//given
        user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_NAME);
        users = new ArrayList<>();
        users.add(user);
        
    	//when
    	when(userService.getAllUsers()).thenReturn(users);
    	
    	//then
        mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].id").value(USER_ID))
        .andExpect(jsonPath("$[0].userName").value(USER_NAME));

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
	public void get_all_users_controller_should_return404_when_userList_isEmpty() throws Exception{
    	//given
    	List<User> users = new ArrayList<User>();
    	//when
    	when(userService.getAllUsers()).thenReturn(users);
    	
    	//then
        mockMvc.perform(get("/users"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE));

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }
    
    @Test
	public void get_user_by_name_controller_should_return200_when_users_isExist() throws Exception{
    	//given
    	User user = new User();
    	user.setId(USER_ID);
    	user.setUserName(USER_NAME);
    	
    	//when
    	when(userService.getUserByName(USER_NAME)).thenReturn(user);
    	
    	//then
    	mockMvc.perform(get("/users").param("name", USER_NAME))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id").value(USER_ID))
        .andExpect(jsonPath("$.userName").value(USER_NAME));

    	verify(userService, times(1)).getUserByName(anyString());
    	verifyNoMoreInteractions(userService);
    }
    @Test
    public void get_user_by_name_controller_should_return404_when_users_is_notExist() throws Exception{
   
    	//when
    	when(userService.getUserByName(USER_NAME)).thenReturn(null);
    	
    	//then
    	mockMvc.perform(get("/users").param("name", USER_NAME))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE));

    	verify(userService, times(1)).getUserByName(anyString());
    	verifyNoMoreInteractions(userService); 
    }
    
    @Test
	public void get_user_by_id_controller_should_return200_when_users_isExist() throws Exception{
    	//given
    	User user = new User();
    	user.setId(USER_ID);
    	user.setUserName(USER_NAME);
    	
    	//when
    	when(userService.getUserById(USER_ID)).thenReturn(user);
    	
    	//then
    	mockMvc.perform(get("/users/{id}", USER_ID))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(jsonPath("$.id").value(USER_ID))
    	.andExpect(jsonPath("$.userName").value(USER_NAME));

    	verify(userService, times(1)).getUserById(anyLong());
    	verifyNoMoreInteractions(userService);
    }
    
    @Test
    public void get_user_by_id_controller_should_return404_when_users_is_notExist() throws Exception{
    	//when
    	when(userService.getUserById(USER_ID)).thenReturn(user);
    	
    	//then
    	mockMvc.perform(get("/users/{id}", USER_ID))
    	.andExpect(status().isNotFound())
    	.andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE));

    	verify(userService, times(1)).getUserById(anyLong());
    	verifyNoMoreInteractions(userService);
    }
    
    @Test
    public void add_user_controller_should_return201_when_user_is_saved() throws Exception{
    	//given
    	User user = new User();
    	user.setUserName(USER_NAME);
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(user);
        
    	//when
    	when(userService.saveUser(new User())).thenReturn(user);
    	
    	//then
    	mockMvc.perform(post("/users").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(status().isCreated())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(jsonPath("$.userName").value(USER_NAME));
    	
    	verify(userService, times(1)).saveUser(any(User.class));
    	verifyNoMoreInteractions(userService);
    }
    
    @Test
    public void add_user_controller_should_return400_when_user_isNull() throws Exception{
    	//given
    	User user = null;
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(user);
          	
    	//then
    	mockMvc.perform(post("/users").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(status().isBadRequest());

    	
    }
}
