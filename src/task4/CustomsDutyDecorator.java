package task4;

public class CustomsDutyDecorator extends PricingDecorator {
    public CustomsDutyDecorator(PricingService wrapped) { super(wrapped); }
    @Override
    public double calculateTotal() {
        double subtotal = super.calculateTotal();
        return java.math.BigDecimal.valueOf(subtotal).multiply(new java.math.BigDecimal("1.085"))
            .setScale(2, java.math.RoundingMode.HALF_UP).doubleValue(); // 8.5% customs tax
    }
    @Override
    public String getReceiptBreakdown() {
        return super.getReceiptBreakdown() + " + [Customs Duty: 8.5%]";
    }
}
