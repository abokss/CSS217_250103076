package task1;

public class BaseFileExporter implements Exporter {
    @Override
    public void export(String destination, String data) {
        System.out.printf("[DISK WRITE] Writing %d bytes to file://%s%n", data.getBytes(java.nio.charset.StandardCharsets.UTF_8).length, destination);
        System.out.println("----- FILE PAYLOAD BEGIN -----");
        System.out.println(data);
        System.out.println("----- FILE PAYLOAD END -----");
    }
}
