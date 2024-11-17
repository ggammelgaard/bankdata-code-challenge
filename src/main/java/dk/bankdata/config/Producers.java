package dk.bankdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.net.http.HttpClient;

@ApplicationScoped
public class Producers {

    @Produces
    @ApplicationScoped
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Produces
    @ApplicationScoped
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}