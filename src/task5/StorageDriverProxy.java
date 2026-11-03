package task5;

public class StorageDriverProxy implements StorageDriver {
    private final StorageDriver realDriver;
    private final UserSession session;
    public StorageDriverProxy(StorageDriver realDriver, UserSession session) {
        if (realDriver == null || session == null) throw new IllegalArgumentException("Dependencies cannot be null");
        this.realDriver = realDriver;
        this.session = session;
    }
    @Override
    public byte[] loadRawBlob(String resourceUri) {
        // Allow only the authenticated roles named in the specification/skeleton.
        String role = session.getRole();
        boolean allowed = "ADMIN".equalsIgnoreCase(role) || "MEMBER".equalsIgnoreCase(role)
            || "ENGINEER".equalsIgnoreCase(role);
        if (!allowed || session.getUsername() == null || session.getUsername().trim().isEmpty()) {
            System.out.printf("[PROXY BLOCKED] Access Denied for User '%s' [Role: %s] on resource: %s%n",
                session.getUsername(), session.getRole(), resourceUri);
            return new byte[0]; // Empty payload on unauthorized access
        }
        System.out.printf("[PROXY AUDIT] Authorized user '%s' [Role: %s] accessing backend: %s%n",
            session.getUsername(), session.getRole(), realDriver.getStorageType());
        return realDriver.loadRawBlob(resourceUri);
    }
    @Override
    public String getStorageType() {
        return "PROXY_GUARDED(" + realDriver.getStorageType() + ")";
    }
}
