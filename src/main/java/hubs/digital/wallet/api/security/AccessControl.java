package hubs.digital.wallet.api.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AccessControl {
    @Before("@annotation(checkAccess)")
    public void checkAccess(JoinPoint joinPoint, CheckAccess checkAccess) {
        AuthenticatedUser user = SecurityContext.getUser();
        if (user == null) {
            log.warn("no user found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not authenticated");
        }

        Long customerId = extractCustomerId(joinPoint);

        if (customerId == null) {
            log.warn("customerId not extracted from method parameters");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "could not extract customerId");
        }

        switch (checkAccess.value()) {
            case CUSTOMER_OWN_RESOURCE -> {
                if (user.role().isEmployee()) {
                    return;
                }
                if (!user.customerId().equals(customerId)) {
                    log.warn("customer {} can not access data of {}", user.customerId(), customerId);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("customer %d  can not access data of %d", user.customerId(), customerId));
                }
            }
            case EMPLOYEE_ONLY -> {
                if (!user.role().isEmployee()) {
                    log.warn("customer {} is not an employee to access", user.customerId());
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you must be employee to access");
                }
            }
        }
    }

    private static Long extractCustomerId(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        Long customerId = null;
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation instanceof PathVariable pathVar) {
                    if ("customerId".equals(pathVar.value()) || pathVar.value().isEmpty()) {
                        customerId = (Long) args[i];
                    }
                }
            }
        }
        return customerId;
    }
}