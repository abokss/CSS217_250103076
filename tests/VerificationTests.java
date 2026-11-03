import task1.*;
import task2.*;
import task3.*;
import task4.*;
import task5.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

public class VerificationTests {
    private static int checks;
    private static void check(boolean ok, String message) {
        checks++;
        if (!ok) throw new AssertionError(message);
    }
    public static void main(String[] args) {
        String json = new JsonFormatter().format("a\"b\n", new String[]{"k"}, new String[][]{{"\\\t\u0001"}});
        check(json.contains("a\\\"b\\n"), "JSON title escaping");
        check(json.contains("\\\\\\t\\u0001"), "JSON control escaping");
        String csv = new CsvFormatter().format("t", new String[]{"k"}, new String[][]{{"a,\"b\"\nc"}});
        check(csv.contains("\"a,\"\"b\"\"\nc\""), "CSV commas, quotes and newline");
        final String[] payload = {null};
        Exporter sink = (destination, data) -> payload[0] = data;
        new Base64EncryptDecorator(new HeaderFooterDecorator(sink, "fixed")).export("x", "Қазақ");
        String encoded = Base64.getEncoder().encodeToString("Қазақ".getBytes(StandardCharsets.UTF_8));
        check(payload[0].startsWith("=== METADATA HEADER: fixed ===\n[BASE64_CIPHERTEXT:" + encoded), "Outer decorator runs first");
        new HeaderFooterDecorator(new Base64EncryptDecorator(sink), "fixed").export("x", "data");
        String decoded = new String(Base64.getDecoder().decode(payload[0].substring("[BASE64_CIPHERTEXT:".length(), payload[0].length()-1)), StandardCharsets.UTF_8);
        check(decoded.contains("METADATA HEADER") && decoded.contains("data"), "Reverse stack encodes metadata");
        AtomicLong clock = new AtomicLong(100);
        final int[] calls = {0};
        StockProvider provider = new StockProvider() {
            public double getMarketPrice(String ticker) { calls[0]++; clock.addAndGet(250); return 183; }
            public String getStatus(String ticker) { return "OPEN"; }
        };
        CachedStockProxy cache = new CachedStockProxy(provider, 1500, clock::get);
        check(cache.getMarketPrice("aapl") == 183, "Cold fetch");
        clock.addAndGet(1499);
        cache.getMarketPrice("AAPL");
        check(calls[0] == 1, "TTL starts after fetch, normalized symbol hits");
        clock.incrementAndGet();
        cache.getMarketPrice("AAPL");
        check(calls[0] == 2, "Exact TTL boundary expires");
        cache.getMarketPrice("GOOGL");
        check(calls[0] == 3, "Cache separates symbols");
        LegacyStockFeed fake = new LegacyStockFeed() {
            public String fetchRawTickerPacket(String ticker) { return " STATUS = OPEN ; ASK = 184 ; BID = 182 "; }
        };
        StockProvider adapter = new StockFeedAdapter(fake);
        check(adapter.getMarketPrice("X") == 183 && adapter.getStatus("X").equals("OPEN"), "Legacy parsing");
        BusinessHoursStrategy policy = new BusinessHoursStrategy();
        check(!policy.shouldDispatch(1,8) && policy.shouldDispatch(1,9) && policy.shouldDispatch(1,17) && !policy.shouldDispatch(1,18), "Business hour boundaries");
        check(policy.shouldDispatch(5,2) && new ImmediateStrategy().shouldDispatch(1,23), "Emergency and immediate dispatch");
        final int[] sends = {0};
        MessageChannel channel = (recipient,message) -> sends[0]++;
        new DailySummaryAlert(channel,policy).processAlert("x","digest",23);
        new DailySummaryAlert(channel,policy).processAlert("x","digest",11);
        new CriticalSystemAlert(channel,policy).processAlert("x","outage",2);
        check(sends[0] == 2,"Suppressed alerts do not reach channel");
        PricingService base = new CurrencyOrderAdapter(new LegacyForeignOrder("test",100), new LegacyCurrencyConverter(),"USD");
        check(base.calculateTotal() == 125,"GBP conversion");
        PricingService total = new CustomsDutyDecorator(new ExpressCourierDecorator(new GiftWrapDecorator(base)));
        check(total.calculateTotal() == 157.33,"Half-up customs rounding");
        check(new GiftWrapDecorator(new CustomsDutyDecorator(base)).calculateTotal() == 140.63,"Decorator order affects taxable amount");
        final int[] loads = {0};
        StorageDriver driver = new StorageDriver() {
            public byte[] loadRawBlob(String path) { loads[0]++; return new byte[]{1}; }
            public String getStorageType() { return "TEST"; }
        };
        for (String role : new String[]{"GUEST", "unknown", null}) {
            check(new StorageDriverProxy(driver,new UserSession("user",role)).loadRawBlob("x").length == 0,"Unapproved role blocked");
        }
        check(loads[0] == 0,"Blocked requests never reach backend");
        for (String role : new String[]{"ADMIN","MEMBER","ENGINEER"}) {
            check(new StorageDriverProxy(driver,new UserSession("user",role)).loadRawBlob("x").length == 1,"Approved role allowed");
        }
        check(loads[0] == 3,"Authorized delegation");
        System.out.println("PASS: " + checks + " verification checks");
    }
}
