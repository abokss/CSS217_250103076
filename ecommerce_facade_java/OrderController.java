package com.ecommerce;

/** Client: obtains abstract products from the selected factory and uses them directly. */
public class OrderController {
    public static void main(String[] args) {
        EcommerceFactory factory;
        if (args.length == 0 || (args.length == 1 && "domestic".equalsIgnoreCase(args[0]))) {
            factory = new DomesticEcommerceFactory();
        } else if (args.length == 1 && "international".equalsIgnoreCase(args[0])) {
            factory = new InternationalEcommerceFactory();
        } else {
            System.err.println("Usage: java com.ecommerce.OrderController [domestic|international]");
            return;
        }

        InventoryService inventory = factory.createInventoryService();
        PaymentGateway payment = factory.createPaymentGateway();
        ShippingService shipping = factory.createShippingService();
        NotificationService notification = factory.createNotificationService();

        String item = "Laptop-X1";
        String customer = "user_404";
        String address = "123 Main St";
        double price = 1200.00;

        System.out.println("Client initiating checkout using Abstract Factory...");
        if (!inventory.checkStock(item)) {
            System.out.println("Order failed: Item out of stock.");
            return;
        }
        inventory.reserve(item);
        if (!payment.charge(customer, price)) {
            System.out.println("Order failed: Payment declined.");
            return;
        }
        shipping.scheduleDelivery(item, address);
        notification.sendReceipt(customer, price);
        System.out.println("Order completed successfully!");
    }
}
