package dk.bankdata.service.ExchangeService;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.bankdata.exception.ExchangeServiceUnavailableException;
import dk.bankdata.model.CurrencyEnum;
import dk.bankdata.model.ExchangeRate;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@QuarkusTest
public class ExchangeServiceTest {

    ExchangeService exchangeService;

    HttpClient httpClient;
    ObjectMapper objectMapper;
    String myVar = "{\"result\":\"success\",\"documentation\":\"https://www.exchangerate-api.com/docs\",\"terms_of_use\":\"https://www.exchangerate-api.com/terms\",\"time_last_update_unix\":1731801601,\"time_last_update_utc\":\"Sun, 17 Nov 2024 00:00:01 +0000\",\"time_next_update_unix\":1731888001,\"time_next_update_utc\":\"Mon, 18 Nov 2024 00:00:01 +0000\",\"base_code\":\"DKK\",\"target_code\":\"USD\",\"conversion_rate\":0.1414}";

    @BeforeEach
    public void setup() {
        httpClient = Mockito.mock(HttpClient.class);
        objectMapper = new ObjectMapper();
        exchangeService = new ExchangeService(httpClient, objectMapper);
    }

    @Test
    public void test_getExchangeRate_OnSuccess_ReturnsSerializedExchangeRate() throws Exception {
        // Arrange
        Path filePath = Paths.get(getClass().getResource("/mockResponse.txt").toURI());
        String expectedResponseBody = Files.readString(filePath);

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.body()).thenReturn(expectedResponseBody);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        // Act
        var result = exchangeService.getExchangeRate("DKK", "USD");

        // Assert
        var expected = new ExchangeRate(CurrencyEnum.DKK, CurrencyEnum.USD, 14.14);
        assertEquals(expected, result);
    }

    @Test
    public void test_getExchangeRate_InvalidCurrency_ThrowsException() {
        String invalidCurrency = "XYZ";

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeService.getExchangeRate(invalidCurrency, "DKK");
        });
    }

    @Test
    public void test_getExchangeRate_ApiUnavailable_ThrowsException() throws Exception {
        String baseInput = "DKK";
        String targetInput = "USD";

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("API unavailable"));

        assertThrows(ExchangeServiceUnavailableException.class, () -> {
            exchangeService.getExchangeRate(baseInput, targetInput);
        });
    }

    @Test
    public void test_computeConversion_OnSuccess_ReturnsConvertedAmount() throws Exception {
        // Arrange
        Path filePath = Paths.get(getClass().getResource("/mockResponse.txt").toURI());
        String expectedResponseBody = Files.readString(filePath);

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.body()).thenReturn(expectedResponseBody);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        // Act
        var result = exchangeService.computeConversion("DKK", "USD", 20);

        // Assert
        assertEquals(2.828, result);
    }
}