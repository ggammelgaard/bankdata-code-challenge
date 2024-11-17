package dk.bankdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ExchangeRateSerializer.class)
public record ExchangeRate(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency, double conversionRate) {
}

