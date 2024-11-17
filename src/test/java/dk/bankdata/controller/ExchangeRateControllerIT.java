package dk.bankdata.controller;

import dk.bankdata.exception.ExchangeServiceUnavailableException;
import dk.bankdata.model.CurrencyEnum;
import dk.bankdata.model.ExchangeRate;
import dk.bankdata.service.ExchangeService.ExchangeService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExchangeRateControllerIT {

    @InjectMock
    private ExchangeService exchangeService;

    @Test
    public void test_getExchangeRate_OnSuccess_Return200() throws ExchangeServiceUnavailableException {
        var mockExchangeRate = new ExchangeRate(CurrencyEnum.DKK, CurrencyEnum.USD, 14.14);
        Mockito.when(exchangeService.getExchangeRate("DKK", "USD")).thenReturn(mockExchangeRate);

        given()
                .pathParam("baseCurrency", "DKK")
                .pathParam("targetCurrency", "USD")
                .when().get("/exchange/{baseCurrency}/{targetCurrency}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(is("{\"DKK\":\"100,00\",\"USD\":\"14,14\"}"));
    }

    @Test
    public void test_getExchangeRate_OnException_ReturnParsedMessage() throws ExchangeServiceUnavailableException {
        Mockito.when(exchangeService.getExchangeRate("CHF", "TRY")).thenThrow(new IllegalArgumentException("Test message"));

        given()
                .pathParam("baseCurrency", "CHF")
                .pathParam("targetCurrency", "TRY")
                .when().get("/exchange/{baseCurrency}/{targetCurrency}")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body(is("{\"message\":\"Test message\"}"));
    }

    // We could test all the ExceptionHandler status codes if we felt it was necessary
}