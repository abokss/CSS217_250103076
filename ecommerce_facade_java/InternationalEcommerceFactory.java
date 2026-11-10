package com.ecommerce;

/** Concrete factory for the international service family. All services are simulations. */
public class InternationalEcommerceFactory implements EcommerceFactory {
    @Override
    public InventoryService createInventoryService() {
        return new InternationalInventory();
    }

    @Override
    public PaymentGateway createPaymentGateway() {
        return new InternationalPayment();
    }

    @Override
    public ShippingService createShippingService() {
        return new InternationalShipping();
    }

    @Override
    public NotificationService createNotificationService() {
        return new InternationalNotification();
    }

    private static class InternationalInventory implements InventoryService {
        @Override
        public boolean checkStock(String productId) {
            System.out.println("[Export warehouse] Checking stock: " + productId);
            return true; // Demonstration only; no inventory database.
        }

        @Override
        public void reserve(String productId) {
            System.out.println("[Export warehouse] Reserved: " + productId);
        }
    }

    private static class InternationalPayment implements PaymentGateway {
        @Override
        public boolean charge(String customerId, double amount) {
            System.out.println("[International payment processor] Charging $" + amount + " to " + customerId);
            return true; // Demonstration only; no real payment is made.
        }
    }

    private static class InternationalShipping implements ShippingService {
        @Override
        public void scheduleDelivery(String productId, String address) {
            System.out.println("[International carrier] Delivering " + productId + " to " + address);
        }
    }

    private static class InternationalNotification implements NotificationService {
        @Override
        public void sendReceipt(String customerId, double amount) {
            System.out.println("[International receipt] Receipt of $" + amount + " sent to " + customerId);
        }
    }
}
