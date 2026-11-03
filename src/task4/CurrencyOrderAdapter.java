package task4;

public class CurrencyOrderAdapter implements PricingService {
    private final LegacyForeignOrder foreignOrder;
    private final LegacyCurrencyConverter converter;
    private final String targetCurrency;
    public CurrencyOrderAdapter(LegacyForeignOrder order, LegacyCurrencyConverter converter, String targetCurrency) {
        this.foreignOrder = order;
        this.converter = converter;
        this.targetCurrency = targetCurrency;
    }
    @Override
    public double calculateTotal() {
        return converter.convert(foreignOrder.getGbpAmount(), "GBP", targetCurrency);
    }
    @Override
    public String getReceiptBreakdown() {
        return String.format("Order [%s] Base Item: $%.2f %s (Converted from %.2f GBP)",
            foreignOrder.getOrderId(), calculateTotal(), targetCurrency, foreignOrder.getGbpAmount());
    }
}
