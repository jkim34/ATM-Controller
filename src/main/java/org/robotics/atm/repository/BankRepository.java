package org.robotics.atm.repository;

import org.robotics.atm.model.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Assuming there may be other bank (or other form/source of repository) in which external engineers can build, this can be a standard template
 */
public interface BankRepository {
    boolean validatePin(String cardNumber, String pin);

    List<Account> findAccounts(String cardNumber);

    boolean deposit(String cardNumber, String accountId, BigDecimal amount);

    boolean withdraw(String cardNumber, String accountId, BigDecimal amount);
}
