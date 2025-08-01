package hubs.digital.wallet.common;

public class Uris {
    public static final String Api_Prefix = "/api/v1";
    public static final String Wallets = "/customers/{customerId}/wallets";
    public static final String Deposit = "/deposit";
    public static final String Withdrawal = "/withdraw";
    public static final String Transactions = Wallets + "/" + "{walletId}/transactions";
}
