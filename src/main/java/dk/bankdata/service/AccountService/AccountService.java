package dk.bankdata.service.AccountService;

import dk.bankdata.exception.AccountNotFoundException;
import dk.bankdata.exception.IncorrectBalanceFormatException;
import dk.bankdata.model.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;

@ApplicationScoped
public class AccountService implements IAccountService {
    @Inject
    EntityManager em;

    public AccountService(EntityManager em) {
        this.em = em;
    }

    @Override
    public double getAccountBalance(Long id) throws AccountNotFoundException {
        return getAccount(id).getBalance();
    }

    @Transactional
    @Override
    public void depositToAccount(Long id, double amountToDeposit) throws AccountNotFoundException {
        getAccount(id).addToBalance(amountToDeposit);
    }

    @Transactional
    @Override
    public Long createAccount(String firstName, String lastName, double balance) {
        var account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setBalance(balance);

        em.persist(account);
        return account.getAccountNumber();
    }

    @Transactional
    @Override
    public void transferAmount(Long fromId, Long toId, double amountToTransfer) throws AccountNotFoundException, IncorrectBalanceFormatException {
        if (amountToTransfer <= 0.0)
            throw new IncorrectBalanceFormatException("Balance of '" + amountToTransfer + "' cannot be processed.");

        getAccount(fromId).addToBalance(-amountToTransfer);
        getAccount(toId).addToBalance(amountToTransfer);
    }

    @NotNull
    private Account getAccount(Long id) throws AccountNotFoundException {
        var account = em.find(Account.class, id);
        if (account == null)
            throw new AccountNotFoundException("Account with ID " + id + " not found.");
        return account;
    }
}
