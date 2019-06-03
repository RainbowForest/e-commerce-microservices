package com.rainbowforest.userservice.service;


import com.rainbowforest.userservice.model.User;
import com.rainbowforest.userservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    private final Long USER_ID = 2L;
    private final String USER_NAME = "test";
    private User user;
    private List<User> userList;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp(){
        user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_NAME);
        userList = new ArrayList<>();
        userList.add(user);
    }

    @Test
    public void get_all_users_test(){
        // Data preparation
        when(userRepository.findAll()).thenReturn(userList);

        // Method call
        List<User> foundUsers = userService.getAllUsers();

        // Verification
        assertEquals(foundUsers.get(0).getUserName(), USER_NAME);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_user_by_id_test(){
        // Data preparation
        when(userRepository.getOne(anyLong())).thenReturn(user);

        // Method call
        User foundUser = userService.getUserById(USER_ID);

        // Verification
        assertEquals(foundUser.getUserName(), USER_NAME);
        Mockito.verify(userRepository, Mockito.times(1)).getOne(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_user_by_name_test(){
        // Data preparation
        when(userRepository.findByUserName(anyString())).thenReturn(user);

        // Method call
        User foundUser = userService.getUserByName(USER_NAME);

        // Verification
        assertEquals(foundUser.getId(), USER_ID);
        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(USER_NAME);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}
