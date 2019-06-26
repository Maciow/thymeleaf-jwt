package com.security.service;

import com.security.exception.NotEnoughProductsInStockException;
import com.security.model.Product;
import com.security.repository.ProductRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartService {
    private ProductRepository productRepository;
    private OrderService orderService;

    public ShoppingCartService(ProductRepository productRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    private Map<Product, Integer> products = new ConcurrentHashMap<>();

    public void addToCart(Long productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isPresent()) {
            if (products.containsKey(product.get())) {
                products.replace(product.get(), products.get(product.get())+1);
            } else {
                products.put(product.get(), 1);
            }
        }
    }

    public void decreaseProductsAmount(Long productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isPresent()) {
            if (products.containsKey(product.get())) {
                if (products.get(product.get()) == 1) {
                    products.remove(product.get());
                } else if (products.get(product.get()) > 1) {
                    products.replace(product.get(), products.get(product.get())-1);
                }
            }
        }
    }

    public void deleteFromCart(Long productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        product.ifPresent(prod -> products.remove(prod));
    }

    public void checkout() throws NotEnoughProductsInStockException {
        Optional<Product> product;
        for (Map.Entry<Product, Integer> entry: products.entrySet()) {
            product = this.productRepository.findById(entry.getKey().getId());
            if (product.isPresent() && product.get().getAvailableQuantity() < entry.getValue()) {
                throw new NotEnoughProductsInStockException(product.get());
            }
            product.ifPresent(product1 -> entry.getKey().setAvailableQuantity(product1.getAvailableQuantity() - entry.getValue()));
        }
        this.orderService.addOrder();
        this.productRepository.saveAll(products.keySet());
        products.clear();
    }

    public Map<Product, Integer> getProductsFromCart() {
        return Collections.unmodifiableMap(products);
    }

    public BigDecimal getTotal() {
        return products.entrySet().stream()
                .map(entry->entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
