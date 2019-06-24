package com.security.repository;

import com.security.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByBrand(String brand, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}
