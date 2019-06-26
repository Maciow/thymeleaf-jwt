package com.security.service;

import com.security.model.Order;
import com.security.model.Product;
import com.security.model.User;
import com.security.repository.OrderRepository;
import com.security.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ShoppingCartService shoppingCartService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.shoppingCartService = shoppingCartService;
    }

    public void addOrder() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = this.userRepository.findByUsername(username);

        Order order = Order.builder()
                .orderState(Order.OrderState.PENDING)
                .products(getProductsOrder())
                .user(user.orElse(null))
                .build();
        this.orderRepository.save(order);
    }

    private Map<Long, Integer> getProductsOrder() {
        Map<Long, Integer> productsOrder = new ConcurrentHashMap<>();
        for (Map.Entry<Product, Integer> products: this.shoppingCartService.getProductsFromCart().entrySet()) {
            productsOrder.put(products.getKey().getId(), products.getValue());
        }
        return productsOrder;
    }
}
