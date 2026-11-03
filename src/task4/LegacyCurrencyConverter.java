package task4;

public class LegacyCurrencyConverter {
    public double convert(double amount, String from, String to) {
        if ("GBP".equalsIgnoreCase(from) && "USD".equalsIgnoreCase(to)) {
            return amount * 1.25; // 1 GBP = 1.25 USD
        }
        return amount;
    }
}
