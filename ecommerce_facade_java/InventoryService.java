package com.ecommerce;

/** Abstract product created by an ecommerce factory. */
public interface InventoryService {
    boolean checkStock(String productId);
    void reserve(String productId);
}
