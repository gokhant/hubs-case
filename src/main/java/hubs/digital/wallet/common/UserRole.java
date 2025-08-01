package hubs.digital.wallet.common;

import java.util.Locale;

public enum UserRole {
    EMPLOYEE, CUSTOMER;

    public static UserRole from(String input) {
        try {
            return valueOf(input.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isEmployee() {
        return this == EMPLOYEE;
    }
}