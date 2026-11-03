package task2;

import java.util.HashMap;
import java.util.Map;

public class CachedStockProxy implements StockProvider {
    private static class CacheEntry {
        final double price;
        final long timestamp;
        CacheEntry(double price, long timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }
    }
    private final StockProvider realProvider;
    private final long ttlMillis;
    private final java.util.function.LongSupplier clock;
    private final Map<String, CacheEntry> priceCache = new HashMap<>();
    public CachedStockProxy(StockProvider realProvider, long ttlMillis) {
        this(realProvider, ttlMillis, () -> System.nanoTime() / 1_000_000L);
    }
    public CachedStockProxy(StockProvider realProvider, long ttlMillis, java.util.function.LongSupplier clock) {
        if (ttlMillis < 0 || clock == null) throw new IllegalArgumentException("Invalid cache configuration");
        this.clock = clock;
        if (realProvider == null) throw new IllegalArgumentException("Real provider cannot be null");
        this.realProvider = realProvider;
        this.ttlMillis = ttlMillis;
    }
    @Override
    public synchronized double getMarketPrice(String ticker) {
        ticker = java.util.Objects.requireNonNull(ticker, "ticker").trim().toUpperCase(java.util.Locale.ROOT);
        long now = clock.getAsLong();
        if (priceCache.containsKey(ticker)) {
            CacheEntry entry = priceCache.get(ticker);
            if ((now - entry.timestamp) < ttlMillis) {
                System.out.printf("[CACHE HIT] Returning cached price for %s: $%.2f (Age: %d ms)%n",
                    ticker, entry.price, (now - entry.timestamp));
                return entry.price;
            }
        }
        System.out.printf("[NETWORK FETCH] Cache miss or expired for %s. Calling upstream adapter...%n", ticker);
        double freshPrice = realProvider.getMarketPrice(ticker);
        priceCache.put(ticker, new CacheEntry(freshPrice, clock.getAsLong()));
        return freshPrice;
    }
    @Override
    public String getStatus(String ticker) {
        return realProvider.getStatus(ticker);
    }
}
