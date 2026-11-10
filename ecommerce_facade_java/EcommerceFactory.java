package com.ecommerce;

/** Abstract Factory: creates a compatible family of checkout services. */
public interface EcommerceFactory {
    InventoryService createInventoryService();
    PaymentGateway createPaymentGateway();
    ShippingService createShippingService();
    NotificationService createNotificationService();
}
