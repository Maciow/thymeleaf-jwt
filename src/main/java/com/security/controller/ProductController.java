package com.security.controller;

import com.security.model.Product;
import com.security.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("products")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String getProducts(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        Page<Product> products;
        if (category != null) {
            products = this.productRepository.findAllByBrand(category, PageRequest.of(page-1, size));
            model.addAttribute("categoryName", category);
        } else {
            products = this.productRepository.findAll(PageRequest.of(page-1,size));
            model.addAttribute("categoryName", "All products");
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getNumber()+1);
        return "products-list";
    }
}
