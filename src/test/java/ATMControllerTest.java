import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.robotics.atm.controller.ATMController;
import org.robotics.atm.model.AccountType;
import org.robotics.atm.repository.InMemoryBankRepository;
import org.robotics.atm.service.AuthenticationService;
import org.robotics.atm.service.BankService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ATMControllerTest {
    private ATMController controller;

    @BeforeEach
    public void setup() {
        InMemoryBankRepository repository = new InMemoryBankRepository();

        controller = new ATMController(
                new AuthenticationService(repository),
                new BankService(repository)
        );
    }

    @Test
    public void shouldReturnBalanceAfterAuthentication() {
        controller.insertCard("1111222233334444");

        assertTrue(controller.enterPin("pin1"));

        assertEquals(
                new BigDecimal(100),
                controller.getBalance(AccountType.CHECKING, "CHK-1")
        );
    }

    @Test
    public void shouldFailAfterAuthenticationFailure() {
        controller.insertCard("1111222233334444");
        assertFalse(controller.enterPin("pin2"));
    }

    @Test
    public void shouldFailBalanceInquiryWithoutAuthentication() {
        controller.insertCard("1111222233334444");

        assertThrows(
                IllegalStateException.class,
                () -> controller.getBalance(AccountType.SAVING, "SAV-1")
        );
    }

    @Test
    public void shouldAllowBalanceInquiryWithAuthentication() {
        controller.insertCard("1111222233334444");
        controller.enterPin("pin1");

        assertEquals(
                new BigDecimal(10000),
                controller.getBalance(AccountType.SAVING, "SAV-1")
        );
    }

    @Test
    public void shouldRejectPinWithoutCardInsertion() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.enterPin("pin1")
        );
    }

    @Test
    void shouldDepositAmountToCheckingAccount() {
        controller.insertCard("1111222233334444");
        controller.enterPin("pin1");

        assertTrue(controller.makeDeposit("1111222233334444", "CHK-1", new BigDecimal("500")));

        assertEquals(
                new BigDecimal("600"),
                controller.getBalance(AccountType.CHECKING, "CHK-1")
        );
    }

    @Test
    void shouldWithrdawAmountFromSavingAccount() {
        controller.insertCard("1111222233334444");
        controller.enterPin("pin1");

        assertTrue(controller.makeWithdraw("1111222233334444", "SAV-1", new BigDecimal("500")));

        assertEquals(
                new BigDecimal("9500"),
                controller.getBalance(AccountType.SAVING, "SAV-1")
        );
    }

    @Test
    void shouldNotWithrdawExceedingAmountFromSavingAccount() {
        controller.insertCard("1111222233334444");
        controller.enterPin("pin1");

        assertFalse(controller.makeWithdraw("1111222233334444", "SAV-1", new BigDecimal("10001")));
    }
}
