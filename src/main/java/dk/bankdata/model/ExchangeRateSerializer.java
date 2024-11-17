package dk.bankdata.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class ExchangeRateSerializer extends JsonSerializer<ExchangeRate> {

    @Override
    public void serialize(ExchangeRate object, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeStartObject();
        generator.writeStringField(object.baseCurrency().name(), String.format("%.2f", 100.00).replace('.', ','));
        generator.writeStringField(object.targetCurrency().name(), String.format("%.2f", object.conversionRate()).replace('.', ','));
        generator.writeEndObject();
    }
}
