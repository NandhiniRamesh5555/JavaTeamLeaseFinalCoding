package com.example.demo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.MessagingConfig;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Product addProduct(Product product) {
        // Business logic for adding a product
        // You can perform validation, additional processing, and then save to the repository
        
    	return productRepository.save(product);
        
    }

    public List<Product> getAllProducts() {
        // Business logic for fetching all products
    	
    	List<Product> productList = productRepository.findAll();
		rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, productList);
		return productList;
		
    }

    public Product selectProduct(Long productId) {
    	
    	 Optional<Product> optionalProduct = productRepository.findById(productId);
    	 if(optionalProduct.isPresent())
    	 {
    		 Product product = optionalProduct.get();
    		 rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,product);
    		 return product;
    	 }
    	 else
    	 {
    		 throw new ResourceNotFoundException(("Product with ID :" + productId + " not found"));
    	 }
         
    }
}
