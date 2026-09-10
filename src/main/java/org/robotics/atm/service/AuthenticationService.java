package org.robotics.atm.service;

import org.robotics.atm.repository.InMemoryBankRepository;

public class AuthenticationService {
    private final InMemoryBankRepository bankRepository;

    public AuthenticationService(InMemoryBankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public boolean authenticate(String cardNumber, String pin) {
        return bankRepository.validatePin(cardNumber, pin);
    }
}
