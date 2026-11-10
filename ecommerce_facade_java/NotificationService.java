package com.ecommerce;

/** Abstract product created by an ecommerce factory. */
public interface NotificationService {
    void sendReceipt(String customerId, double amount);
}
