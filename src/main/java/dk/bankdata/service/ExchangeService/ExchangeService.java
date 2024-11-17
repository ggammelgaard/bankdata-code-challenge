package dk.bankdata.service.ExchangeService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.bankdata.exception.ExchangeServiceUnavailableException;
import dk.bankdata.model.CurrencyEnum;
import dk.bankdata.model.ExchangeRate;
import dk.bankdata.model.ExhangeRateApiResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class ExchangeService implements IExchangeService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "https://v6.exchangerate-api.com/v6/4545307c4d5bedb4966382b7/pair/";

    public ExchangeService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    private static String getCurrencyUrl(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) {
        return baseCurrency.name() + "/" + targetCurrency.name();
    }

    @Override
    public ExchangeRate getExchangeRate(String baseInput, String targetInput) throws ExchangeServiceUnavailableException {
        var baseCurrency = validateAndParseInput(baseInput); // These two methods should be merged and return a tuple
        var targetCurrency = validateAndParseInput(targetInput);

        ExhangeRateApiResponse exchangeRateResponse = requestExchangeRateFromApi(baseCurrency, targetCurrency);
        return buildExchangeRateObject(exchangeRateResponse, baseCurrency, targetCurrency);
    }

    @Override
    public double computeConversion(String baseInput, String targetInput, double amount) throws ExchangeServiceUnavailableException {
        var baseCurrency = validateAndParseInput(baseInput); // These two methods should be merged and return a tuple
        var targetCurrency = validateAndParseInput(targetInput);

        ExhangeRateApiResponse exchangeRateResponse = requestExchangeRateFromApi(baseCurrency, targetCurrency);
        return exchangeRateResponse.getConversionRate() * amount;
    }

    private ExhangeRateApiResponse requestExchangeRateFromApi(CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) throws ExchangeServiceUnavailableException {
        String currencyUrl = getCurrencyUrl(baseCurrency, targetCurrency);
        HttpRequest request = buildRequest(currencyUrl);
        HttpResponse<String> response = sendRequest(request);
        return parseResponse(response);
    }

    private CurrencyEnum validateAndParseInput(String currencyString) {
        try {
            return CurrencyEnum.valueOf(currencyString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Currency not supported.");
        }
    }

    private HttpRequest buildRequest(String currencyUrl) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + currencyUrl))
                .GET()
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ExchangeServiceUnavailableException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ExchangeServiceUnavailableException("Unable to fetch exchange rate from external service.");
        }
    }

    private ExhangeRateApiResponse parseResponse(HttpResponse<String> response) throws ExchangeServiceUnavailableException {
        try {
            return objectMapper.readValue(response.body(), new TypeReference<ExhangeRateApiResponse>() {
            });
        } catch (IOException e) {
            throw new ExchangeServiceUnavailableException("Error parsing exchange rate response.");
        }
    }

    private ExchangeRate buildExchangeRateObject(ExhangeRateApiResponse exchangeRateResponse, CurrencyEnum baseCurrency, CurrencyEnum targetCurrency) {
        double conversionRate = exchangeRateResponse.getConversionRate() * 100;
        return new ExchangeRate(baseCurrency, targetCurrency, conversionRate);
    }
}