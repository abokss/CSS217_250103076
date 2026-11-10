package com.ecommerce;

/** Abstract product created by an ecommerce factory. */
public interface ShippingService {
    void scheduleDelivery(String productId, String address);
}
