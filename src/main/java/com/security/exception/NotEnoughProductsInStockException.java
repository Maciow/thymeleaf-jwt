package com.security.exception;

import com.security.model.Product;

public class NotEnoughProductsInStockException extends Exception{
    public NotEnoughProductsInStockException(Product product) {
        super(String.format("Not enough %s in stock. Only %d left",product.getName(),product.getAvailableQuantity()));
    }
}
