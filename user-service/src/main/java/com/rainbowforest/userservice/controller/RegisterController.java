package com.rainbowforest.userservice.controller;

import com.rainbowforest.userservice.model.User;
import com.rainbowforest.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registration")
    public ResponseEntity<User> addUser(@RequestBody User user, HttpServletRequest request) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(request.getRequestURI() + "/" + user.getId()));
        userService.saveUser(user);
        return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
    }
}
