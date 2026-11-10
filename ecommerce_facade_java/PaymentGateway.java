package com.ecommerce;

/** Abstract product created by an ecommerce factory. */
public interface PaymentGateway {
    boolean charge(String customerId, double amount);
}
