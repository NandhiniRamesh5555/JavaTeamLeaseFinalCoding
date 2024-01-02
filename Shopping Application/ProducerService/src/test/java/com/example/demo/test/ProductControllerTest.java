package com.example.demo.test;

import com.example.demo.controller.ProductController;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Pen");
        product.setProductDescription("writing");
        product.setPrice(25.0);

        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/add")
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> productList = Arrays.asList(
                new Product("Pen", "writing", 10.0),
                new Product("Car", "driving", 2000.0)
        );

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(productList.size()));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testSelectProductById() throws Exception {
        long productId = 1L;
        Product product = new Product("Nintendo", "gaming", 500.0);

        when(productService.selectProduct(productId)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", productId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value(product.getProductName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productDescription").value(product.getProductDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(product.getPrice()));

        verify(productService, times(1)).selectProduct(productId);
    }
    
    
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}