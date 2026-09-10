package org.robotics.atm.service;

import org.robotics.atm.repository.BankRepository;

public class AuthenticationService {
    private final BankRepository bankRepository;

    public AuthenticationService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public boolean authenticate(String cardNumber, String pin) {
        return bankRepository.validatePin(cardNumber, pin);
    }
}
