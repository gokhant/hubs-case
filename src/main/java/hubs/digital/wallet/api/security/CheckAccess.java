package hubs.digital.wallet.api.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAccess {
    AccessType value();

    enum AccessType {
        CUSTOMER_OWN_RESOURCE,
        EMPLOYEE_ONLY
    }
}