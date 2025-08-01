package hubs.digital.wallet.api.security;

import hubs.digital.wallet.common.UserRole;

public record AuthenticatedUser(String userName, UserRole role, Long customerId) {
}