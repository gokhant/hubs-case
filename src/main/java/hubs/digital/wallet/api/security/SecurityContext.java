package hubs.digital.wallet.api.security;

public class SecurityContext {
    private static final ThreadLocal<AuthenticatedUser> userHolder = new ThreadLocal<>();

    public static void setUser(AuthenticatedUser user) {
        userHolder.set(user);
    }

    public static AuthenticatedUser getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}