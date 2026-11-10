package com.ecommerce;

/** Concrete factory for the domestic service family. All services are simulations. */
public class DomesticEcommerceFactory implements EcommerceFactory {
    @Override
    public InventoryService createInventoryService() {
        return new DomesticInventory();
    }

    @Override
    public PaymentGateway createPaymentGateway() {
        return new DomesticPayment();
    }

    @Override
    public ShippingService createShippingService() {
        return new DomesticShipping();
    }

    @Override
    public NotificationService createNotificationService() {
        return new DomesticNotification();
    }

    private static class DomesticInventory implements InventoryService {
        @Override
        public boolean checkStock(String productId) {
            System.out.println("[Local warehouse] Checking stock: " + productId);
            return true; // Demonstration only; no inventory database.
        }

        @Override
        public void reserve(String productId) {
            System.out.println("[Local warehouse] Reserved: " + productId);
        }
    }

    private static class DomesticPayment implements PaymentGateway {
        @Override
        public boolean charge(String customerId, double amount) {
            System.out.println("[Domestic payment processor] Charging $" + amount + " to " + customerId);
            return true; // Demonstration only; no real payment is made.
        }
    }

    private static class DomesticShipping implements ShippingService {
        @Override
        public void scheduleDelivery(String productId, String address) {
            System.out.println("[Local courier] Delivering " + productId + " to " + address);
        }
    }

    private static class DomesticNotification implements NotificationService {
        @Override
        public void sendReceipt(String customerId, double amount) {
            System.out.println("[Domestic receipt] Receipt of $" + amount + " sent to " + customerId);
        }
    }
}
