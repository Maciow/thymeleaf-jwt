package com.security.controller;

import com.security.exception.NotEnoughProductsInStockException;
import com.security.service.OrderService;
import com.security.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String REFERER = "Referer";

    private ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, OrderService orderService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/add-to-cart")
    public String addToCart(@RequestParam Long id,
                            HttpServletRequest request) {
        this.shoppingCartService.addToCart(id);
        return REDIRECT_PREFIX + request.getHeader(REFERER);
    }

    @GetMapping("/reduce-amount")
    public String reduceProductAmount(@RequestParam Long id, HttpServletRequest request) {
        this.shoppingCartService.decreaseProductsAmount(id);
        return REDIRECT_PREFIX + request.getHeader(REFERER);
    }

    @GetMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam Long id, HttpServletRequest request) {
        this.shoppingCartService.deleteFromCart(id);
        return REDIRECT_PREFIX + request.getHeader(REFERER);
    }

    @GetMapping("/cart")
    public String cart(Model model, @ModelAttribute String productsException) {
        model.addAttribute("products", this.shoppingCartService.getProductsFromCart());
        model.addAttribute("total", this.shoppingCartService.getTotal());
        if (!productsException.equals("")) {
            model.addAttribute("productsException", productsException);
        }
        return "cart";
    }

    @PostMapping("/checkout")
    public String checkout(RedirectAttributes attributes) {
        try {
            this.shoppingCartService.checkout();
        } catch (NotEnoughProductsInStockException e) {
            attributes.addFlashAttribute("productsException", e.getMessage());
            return REDIRECT_PREFIX + "cart";
        }
        return REDIRECT_PREFIX + "/";
    }
}
