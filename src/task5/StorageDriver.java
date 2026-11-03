package task5;

public interface StorageDriver {
    byte[] loadRawBlob(String resourceUri);
    String getStorageType();
}
