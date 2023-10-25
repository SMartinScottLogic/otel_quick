package com.example.springboot.service;

import com.example.springboot.model.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceService {
    public Price getPrice(long productId) {
        Price price = new Price();
        price.setProductId(productId);
        price.setPriceAmount(1.10);
        price.setDiscount(0.95);
        return price;
    }
}
