package com.example.demo.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.MessagingConfig;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product prod) {
        
    	Product product = productService.addProduct(prod);
        
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> displayAllProducts() {
    	
    	
    	List<Product> products = productService.getAllProducts();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
        
    }

    @PostMapping("/select/{id}")
    public ResponseEntity<?> selectProduct(@PathVariable Long id) {
    	
    	Product product = productService.selectProduct(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    
    }
}