package com.example.springboot.repository;

import com.example.springboot.exception.ProductNotFoundException;
import com.example.springboot.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepository.class);

    private final Map<Long, Product> productMap = new HashMap<>();
    private final Tracer tracer;
    private final ObjectMapper objectMapper;
    ProductRepository(io.opentelemetry.api.OpenTelemetry openTelemetry) {
        objectMapper = new ObjectMapper();
        tracer = openTelemetry.getTracer(this.getClass().getName());
    }

public Product getProduct(Long productId) throws JsonProcessingException {
        var span = tracer.spanBuilder("getProduce").setAttribute("productId", productId).startSpan();

        try {
            LOGGER.info("Getting Product from Product Repo With Product Id {}", productId);
            span.addEvent("Getting Product from Product Repo with Product Id " + productId);
            if (!productMap.containsKey(productId)) {
                LOGGER.error("Product Not Found for Product Id {}", productId);
                span.addEvent("Product Not Found for Product Id " + productId);
                throw new ProductNotFoundException("Product Not Found");
            }

            var product = productMap.get(productId);

            span.addEvent("test", Attributes.empty().toBuilder().put("Product Id", productId).put("another", "string").put("pet", 1.75).put("test", objectMapper.writeValueAsString(product)).build());

            span.setAttribute("result", objectMapper.writeValueAsString(product));
            return product;
        } catch (Exception e) {
            span.recordException(e).setStatus(StatusCode.ERROR, e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }

    @PostConstruct
    private void setupRepo() {
        Product product1 = getProduct(100001, "apple");
        productMap.put(100001L, product1);

        Product product2 = getProduct(100002, "pears");
        productMap.put(100002L, product2);

        Product product3 = getProduct(100003, "banana");
        productMap.put(100003L, product3);

        Product product4 = getProduct(100004, "mango");
        productMap.put(100004L, product4);

        Product product5 = getProduct(100005, "test");
        productMap.put(100005L, product5);
    }

    private static Product getProduct(int id, String name) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        return product;
    }
}
