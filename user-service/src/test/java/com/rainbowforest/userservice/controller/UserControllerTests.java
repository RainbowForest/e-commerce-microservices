package com.rainbowforest.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainbowforest.userservice.model.User;
import com.rainbowforest.userservice.service.UserService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    private final Long USER_ID = 2L;
    private final String USER_NAME = "test";
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Before
    public void setUp(){
        user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_NAME);
        userList = new ArrayList<>();
        userList.add(user);
    }

    @Test
    public void get_all_users_controller_test() throws Exception {
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].userName").value(USER_NAME))
                .andDo(print());

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void get_user_by_name_controller_test() throws Exception {
        when(userService.getUserByName(anyString())).thenReturn(user);

        mockMvc.perform(get("/users").param("name", USER_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.userName").value(USER_NAME))
                .andDo(print());

        verify(userService, times(1)).getUserByName(anyString());
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void get_user_by_id_controller_test() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.userName").value(USER_NAME))
                .andDo(print());

        verify(userService, times(1)).getUserById(anyLong());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void add_user_controller_test() throws Exception {
        when(userService.saveUser(new User())).thenReturn(user);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users").content(userJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.userName").value(USER_NAME))
                .andDo(print());

        verify(userService, times(1)).saveUser(any(User.class));
        verifyNoMoreInteractions(userService);
    }
}
