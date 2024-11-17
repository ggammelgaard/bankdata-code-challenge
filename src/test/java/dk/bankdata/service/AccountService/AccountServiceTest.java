package dk.bankdata.service.AccountService;

import dk.bankdata.exception.AccountNotFoundException;
import dk.bankdata.exception.IncorrectBalanceFormatException;
import dk.bankdata.model.Account;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AccountServiceTest {

    AccountService accountService;

    EntityManager em;

    @BeforeEach
    public void setup() {
        em = Mockito.mock(EntityManager.class);
        accountService = new AccountService(em);
    }

    @Test
    public void test_getAccountBalance_OnSuccess_BalanceIsReturn() throws AccountNotFoundException {
        Account account = new Account();
        account.setBalance(100.0);
        when(em.find(Account.class, 1L)).thenReturn(account);

        double balance = accountService.getAccountBalance(1L);
        assertEquals(100.0, balance);
    }

    @Test
    public void test_getAccountBalance_NonExistentAccount_ThrowsException() {
        when(em.find(Account.class, 1L)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountBalance(1L);
        });
    }

    @Test
    @Transactional
    public void test_depositToAccount_OnSuccess_BalanceIsUpdated() throws AccountNotFoundException {
        Account account = new Account();
        account.setBalance(100.0);
        when(em.find(Account.class, 1L)).thenReturn(account);

        accountService.depositToAccount(1L, 50.0);
        assertEquals(150.0, account.getBalance());
    }

    @Test
    @Transactional
    public void test_createAccount_OnSuccess_EmIsCalled() {
        // Arrange
        String firstName = "Lars";
        String lastName = "Larsen";
        double initialBalance = 100.0;

        // Act
        accountService.createAccount(firstName, lastName, initialBalance);

        // Capture the Account object passed to the EntityManager
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(em).persist(accountCaptor.capture());

        // Assert
        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(firstName, capturedAccount.getFirstName());
        assertEquals(lastName, capturedAccount.getLastName());
        assertEquals(initialBalance, capturedAccount.getBalance());
    }

    @Test
    @Transactional
    public void test_transferAmount_OnSuccess_AccountsAreUpdated() throws AccountNotFoundException, IncorrectBalanceFormatException {
        Account fromAccount = new Account();
        fromAccount.setBalance(50.0);
        Account toAccount = new Account();
        toAccount.setBalance(100.0);

        when(em.find(Account.class, 1L)).thenReturn(fromAccount);
        when(em.find(Account.class, 2L)).thenReturn(toAccount);

        accountService.transferAmount(1L, 2L, 50.0);

        assertEquals(0.0, fromAccount.getBalance());
        assertEquals(150.0, toAccount.getBalance());
    }

    @Test
    public void test_transferAmount_OnNegativeAmount_ThrowsException() {
        assertThrows(IncorrectBalanceFormatException.class, () -> {
            accountService.transferAmount(1L, 2L, -50.0);
        });
    }
}