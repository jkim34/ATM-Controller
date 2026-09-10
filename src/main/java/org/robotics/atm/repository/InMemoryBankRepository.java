package org.robotics.atm.repository;

import org.robotics.atm.model.Account;
import org.robotics.atm.model.AccountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For simulation
 */
public class InMemoryBankRepository implements BankRepository {

    private final Map<String, String> pins = new ConcurrentHashMap<>();
    private final Map<String, List<Account>> accounts = new ConcurrentHashMap<>();

    public InMemoryBankRepository() {
        pins.put("1111222333444", "pin1");
        accounts.put("1111222333444", List.of(
                new Account("CHK-1", AccountType.CHECKING, new BigDecimal(100)),
                new Account("SAV-1", AccountType.SAVING, new BigDecimal(10000)),
                new Account("SAV-2", AccountType.SAVING, new BigDecimal(100000))
        ));
    }

    @Override
    public boolean validatePin(String cardNumber, String pin) {
        return pin.equals(pins.get(cardNumber));
    }

    @Override
    public List<Account> findAccounts(String cardNumber) {
        return accounts.getOrDefault(cardNumber, List.of());
    }

    @Override
    public boolean deposit(String cardNumber, String accountId, BigDecimal amount) {
        var currentAccounts = findAccounts(cardNumber);
        if (currentAccounts.isEmpty()) {
            return false;
        }

        var updatedAccounts = currentAccounts.stream()
                .map(account -> {
                    if (account.accountId().equals(accountId)) {
                        BigDecimal newBalance = account.balance().add(amount);
                        return new Account(account.accountId(), account.type(), newBalance);
                    }
                    return account;
                })
                .toList();

        accounts.put(cardNumber, updatedAccounts);
        return true;
    }

    @Override
    public boolean withdraw(String cardNumber, String accountId, BigDecimal amount) {
        var currentAccounts = findAccounts(cardNumber);
        if (currentAccounts.isEmpty()) {
            return false;
        }

        boolean isWithdrawn = false;
        var updatedAccounts = new ArrayList<Account>();
        for (Account account : currentAccounts) {
            if (account.accountId().equals(accountId) && account.balance().compareTo(amount) >= 0) { // > 0 means balance is greater than or equal to amount
                BigDecimal newBalance = account.balance().subtract(amount);
                updatedAccounts.add(new Account(account.accountId(), account.type(), newBalance));

                isWithdrawn = true;
            } else {
                updatedAccounts.add(account);
            }
        }

        accounts.put(cardNumber, updatedAccounts);
        return isWithdrawn;
    }
}
