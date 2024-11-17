package dk.bankdata.service.ExchangeService;

import dk.bankdata.exception.ExchangeServiceUnavailableException;
import dk.bankdata.model.ExchangeRate;

public interface IExchangeService {
    ExchangeRate getExchangeRate(String baseInput, String targetInput) throws ExchangeServiceUnavailableException;

    double computeConversion(String baseCurrency, String targetCurrency, double amount) throws ExchangeServiceUnavailableException;
}
