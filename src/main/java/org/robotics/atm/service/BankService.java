package org.robotics.atm.service;

import org.robotics.atm.model.Account;
import org.robotics.atm.model.AccountType;
import org.robotics.atm.repository.BankRepository;

import java.math.BigDecimal;
import java.util.List;

public class BankService {
    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Account> getAccounts(String cardNumber) {
        return bankRepository.findAccounts(cardNumber);
    }

    public BigDecimal getBalance(String cardNumber, AccountType accountType, String accountId) {
        return bankRepository.findAccounts(cardNumber)
                .stream()
                .filter(account -> account.type().equals(accountType))
                .filter(account -> account.accountId().equals(accountId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"))
                .balance();
    }

    public boolean withdraw(String cardNumber, String accountId, BigDecimal amount) {
        return bankRepository.withdraw(cardNumber, accountId, amount);
    }

    public boolean deposit(String cardNumber, String accountId, BigDecimal amount) {
        return bankRepository.deposit(cardNumber, accountId, amount);
    }
}
