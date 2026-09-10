package org.robotics.atm.controller;

import org.robotics.atm.model.Account;
import org.robotics.atm.model.AccountType;
import org.robotics.atm.model.Card;
import org.robotics.atm.service.AuthenticationService;
import org.robotics.atm.service.BankService;

import java.math.BigDecimal;
import java.util.List;

public class ATMController {
    private final AuthenticationService authenticationService;
    private final BankService bankService;

    private Card insertedCard;
    private boolean authenticated;

    public ATMController(AuthenticationService authenticationService, BankService bankService) {
        this.authenticationService = authenticationService;
        this.bankService = bankService;
    }

    public void insertCard(String cardNumber) {
        if (insertedCard != null) {
            throw new IllegalStateException("Already inserted card");
        }

        insertedCard = new Card(cardNumber);
        authenticated = false;
    }

    public boolean enterPin(String pin) {
        if (authenticated) {
            // Already authenticated. Would this be a possible scenario?
            return authenticated;
        }

        authenticated = authenticationService.authenticate(
                insertedCard.cardNumber(),
                pin
        );

        return authenticated;
    }

    public List<Account> getAccounts() {
        ensureAuthenticated();

        return bankService.getAccounts(
                insertedCard.cardNumber()
        );
    }

    // Assuming user has access to accountId via screen
    public BigDecimal getBalance(AccountType accountType, String accountId) {
        ensureAuthenticated();

        return bankService.getBalance(
                insertedCard.cardNumber(),
                accountType,
                accountId
        );
    }

    public void ejectCard() {
        insertedCard = null;
        authenticated = false;
    }

    private void ensureAuthenticated() {
        if (insertedCard == null) {
            throw new IllegalStateException("No card inserted");
        }

        if (!authenticated) {
            throw new IllegalStateException("Not authenticated");
        }
    }
}
