package dk.bankdata.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ExchangeRateSerializerRealTest {

    @Test
    public void test_serialize_OnSuccess_ReturnJson() throws IOException {
        // Arrange
        ExchangeRate exchangeRate = new ExchangeRate(CurrencyEnum.DKK, CurrencyEnum.USD, 0.85);
        ExchangeRateSerializer serializer = new ExchangeRateSerializer();

        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        // Act
        serializer.serialize(exchangeRate, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        // Assert
        String expectedJson = "{\"DKK\":\"100,00\",\"USD\":\"0,85\"}";
        assertEquals(expectedJson, writer.toString());
    }
}