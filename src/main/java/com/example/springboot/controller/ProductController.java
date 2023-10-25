package com.example.springboot.controller;

import com.example.springboot.model.Product;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.service.PriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final PriceService priceService;

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(PriceService priceService, ProductRepository productRepository) {
        this.priceService = priceService;
        this.productRepository = productRepository;
    }

    @GetMapping(path = "/product/{id}")
    public Product getProductDetails(@PathVariable("id") long productId) throws JsonProcessingException {
        LOGGER.info("Getting Product and Price Details With Product Id {}", productId);
        Product product = productRepository.getProduct(productId);
        product.setPrice(priceService.getPrice(productId));

        return product;
    }
}