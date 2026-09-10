package org.robotics.atm.model;

import java.math.BigDecimal;

public record Account(String accountId, AccountType type, BigDecimal balance) {
}
