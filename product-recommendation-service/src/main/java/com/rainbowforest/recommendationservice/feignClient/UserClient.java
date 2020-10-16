package com.rainbowforest.recommendationservice.feignClient;

import com.rainbowforest.recommendationservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient (name = "User", url = "http://localhost:8811/")
public interface UserClient {

    @GetMapping (value = "/users/{id}")
    public User getUserById(@PathVariable ("id") Long id);
}
