package dk.bankdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.bankdata.exception.ExchangeServiceUnavailableException;
import dk.bankdata.model.ExchangeRate;
import dk.bankdata.service.ExchangeService.IExchangeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestQuery;

import java.io.IOException;

@Path("/exchange")
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeRateController {

    @Inject
    private IExchangeService exchangeService;

    @Inject
    private ObjectMapper objectMapper;

    @GET
    @Path("{baseCurrency}/{targetCurrency}")
    public Response getExchangeRate(String baseCurrency, String targetCurrency) throws ExchangeServiceUnavailableException {
        var exchangeRate = exchangeService.getExchangeRate(baseCurrency, targetCurrency);
        return Response.status(Response.Status.OK).entity(serializeExchangeRateObject(exchangeRate)).build();
    }

    @POST
    @Path("{baseCurrency}/{targetCurrency}")
    public Response computeConversion(String baseCurrency, String targetCurrency, @RestQuery double amount) throws ExchangeServiceUnavailableException {
        var convertedAmount = exchangeService.computeConversion(baseCurrency, targetCurrency, amount);
        return Response.status(Response.Status.OK).entity(convertedAmount).build();
    }

    private String serializeExchangeRateObject(ExchangeRate exchangeRate) throws ExchangeServiceUnavailableException {
        try {
            return objectMapper.writeValueAsString(exchangeRate);
        } catch (IOException e) {
            throw new ExchangeServiceUnavailableException("Error serializing exchange rate.");
        }
    }
}
