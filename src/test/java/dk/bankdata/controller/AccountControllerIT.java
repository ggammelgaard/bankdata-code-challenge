package dk.bankdata.controller;

import dk.bankdata.exception.AccountNotFoundException;
import dk.bankdata.exception.IncorrectBalanceFormatException;
import dk.bankdata.service.AccountService.AccountService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AccountControllerIT {

    @InjectMock
    private AccountService accountService;

    @Test
    public void test_getAccountBalance_OnSuccess_ReturnAccountBalance() throws AccountNotFoundException {
        Mockito.when(accountService.getAccountBalance(1L)).thenReturn(3.3);

        given()
                .pathParam("id", 1)
                .when().get("/account/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(is("3.3"));
    }

    @Test
    public void test_getAccountBalance_OnAccountNotFound_Return404() throws AccountNotFoundException {
        Mockito.when(accountService.getAccountBalance(9L)).thenThrow(new AccountNotFoundException("Account not found"));

        given()
                .pathParam("id", 9)
                .when().get("/account/{id}")
                .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body(is("{\"message\":\"Account not found\"}"));
    }

    @Test
    public void test_depositToAccount_OnIncorrectBalance_Return400() throws AccountNotFoundException {
        Mockito.doThrow(new AccountNotFoundException("Account not found")).when(accountService).depositToAccount(1L, -5.0);

        given()
                .pathParam("id", 1)
                .queryParam("amountToDeposit", -5)
                .when().put("/account/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    public void test_depositToAccount_OnSuccess_Return202() {
        given()
                .pathParam("id", 1)
                .queryParam("amountToDeposit", 500.0)
                .when().put("/account/{id}")
                .then()
                .statusCode(202);
    }

    @Test
    public void test_transferAmount_OnSuccess_Return202() throws AccountNotFoundException, IncorrectBalanceFormatException {
        Mockito.doNothing().when(accountService).transferAmount(1L, 2L, 300.0);

        given()
                .queryParam("fromId", 1)
                .queryParam("toId", 2)
                .queryParam("amountToTransfer", 300.0)
                .when().put("/account/transfer")
                .then()
                .statusCode(202);
    }

    @Test
    public void test_createAccount_OnSuccess_ReturnAccountNumber() {
        Mockito.when(accountService.createAccount("Peter", "Petersen", 100.0)).thenReturn(9L);

        given()
                .queryParam("firstName", "Peter")
                .queryParam("lastName", "Petersen")
                .queryParam("balance", 100.0)
                .when().post("/account")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(201)
                .body(is("9"));
    }
}