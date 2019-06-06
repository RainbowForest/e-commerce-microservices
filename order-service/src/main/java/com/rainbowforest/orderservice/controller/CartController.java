package com.rainbowforest.orderservice.controller;

import com.rainbowforest.orderservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping (value = "/cart")
    private ResponseEntity<List<Object>> getCart(@RequestHeader(value = "Cookie") String cartId){
        List<Object> cart = cartService.getCart(cartId);
        return new ResponseEntity<List<Object>>(cart, HttpStatus.OK);
    }

    @PostMapping(value = "/cart", params = {"productId", "quantity"})
    private ResponseEntity addItemToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity,
            @RequestHeader(value = "Cookie") String cartId) {
        List<Object> cart = cartService.getCart(cartId);
        if(cart.isEmpty()){
            cartService.addItemToCart(cartId, productId, quantity);
        }else{
            if(cartService.checkIfItemIsExist(cartId, productId)){
                cartService.changeItemQuantity(cartId, productId, quantity);
            }else {
                cartService.addItemToCart(cartId, productId, quantity);
            }
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/cart", params = "productId")
    private ResponseEntity removeItemFromCart(
            @RequestParam("productId") Long productId,
            @RequestHeader(value = "Cookie") String cartId){
        cartService.deleteItemFromCart(cartId, productId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
