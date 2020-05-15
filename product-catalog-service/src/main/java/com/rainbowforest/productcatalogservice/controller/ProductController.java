package com.rainbowforest.productcatalogservice.controller;

import com.rainbowforest.productcatalogservice.model.Product;
import com.rainbowforest.productcatalogservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping (value = "/products")
    public List<Product> getAllProducts(){
        return productService.getAllProduct();
    }

    @GetMapping(value = "/products", params = "category")
    public List<Product> getAllProductByCategory(@RequestParam ("category") String category){
        return productService.getAllProductByCategory(category);
    }

    @GetMapping (value = "/products/{id}")
    public Product getOneProductById(@PathVariable ("id") long id){
        return productService.getOneById(id);
    }

    @GetMapping (value = "/products", params = "name")
    public List<Product> getAllProductsByName(@RequestParam ("name") String name){
        return productService.getAllProductsByName(name);
    }
}
