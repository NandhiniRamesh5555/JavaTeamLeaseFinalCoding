package com.example.demo.controller;


import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Product;

@Controller
public class ConsumerController {

    @RabbitListener(queues = "rabbit_queue")
    public void listenListOfProducts(List<Product> productList) {
    	
        System.out.println("Received RabbitMQ message - List of Products: " + productList);
      
    }

    @RabbitListener(queues = "rabbit_queue")
    public void listenSelectProduct(Product product) {

        System.out.println("Received RabbitMQ message - Product selected: " + product);
    }
}
