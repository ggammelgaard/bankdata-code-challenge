package dk.bankdata.service.AccountService;

import dk.bankdata.exception.AccountNotFoundException;
import dk.bankdata.exception.IncorrectBalanceFormatException;
import jakarta.transaction.Transactional;

public interface IAccountService {
    double getAccountBalance(Long id) throws AccountNotFoundException;

    @Transactional
    void depositToAccount(Long id, double amountToDeposit) throws AccountNotFoundException;

    @Transactional
    Long createAccount(String firstName, String lastName, double balance);

    @Transactional
    void transferAmount(Long fromId, Long toId, double amountToTransfer) throws AccountNotFoundException, IncorrectBalanceFormatException;
}
