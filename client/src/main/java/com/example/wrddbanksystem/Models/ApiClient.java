package com.example.wrddbanksystem.Models;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import com.example.wrddbanksystem.Config.AppConfig;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//----- Api Client Class -----
public class ApiClient {
    private static final AppConfig CONFIG = AppConfig.getInstance();
    private static final String BASE_URL = CONFIG.getApiUrl();
    
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(CONFIG.getConnectTimeout()))
            .build();
    
    private static final ObjectMapper objectMapper = createObjectMapper();
    
    //----- Initialize ObjectMapper with proper configuration -----
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            
        //----- Prevent Circular References -----
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        
        //----- Set Reasonable Nesting Depth Limit -----
        StreamReadConstraints streamReadConstraints = StreamReadConstraints.builder()
            .maxNestingDepth(100)
            .build();
        mapper.getFactory().setStreamReadConstraints(streamReadConstraints);
        
        return mapper;
    }
    
    //----- Authentication Token For API Requests -----
    private static String authToken;
    
    //----- Set The Authentication Token -----
    public static void setAuthToken(String token) {
        authToken = token;
    }
    
    //----- Get The Current Authentication Token -----
    public static String getAuthToken() {
        return authToken;
    }
    
    //----- Clear The Authentication Token -----
    public static void clearAuthToken() {
        authToken = null;
    }
    
    //----- Generic Method To Send A GET Request -----
    public static <T> T get(String endpoint, Class<T> responseType) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + endpoint));
        
        //----- Add Authentication Token If Available -----
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (CONFIG.isLoggingEnabled()) {
            System.out.println("GET " + BASE_URL + endpoint + " -> " + response.statusCode());
        }
        
        if (response.statusCode() != 200) {
            throw new IOException("GET request failed: " + response.statusCode() + 
                                  "\nResponse: " + response.body());
        }
        
        return objectMapper.readValue(response.body(), responseType);
    }
    
    //----- Generic Method To Send A POST Request -----
    public static <T> T post(String endpoint, Object requestBody, Class<T> responseType) 
            throws IOException, InterruptedException {
        String jsonBody = requestBody != null ? objectMapper.writeValueAsString(requestBody) : null;
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""))
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json");
        
        //----- Add Authentication Token If Available -----
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (CONFIG.isLoggingEnabled()) {
            System.out.println("POST " + BASE_URL + endpoint + " -> " + response.statusCode());
        }
        
        if (response.statusCode() >= 400) {
            throw new IOException("POST request failed: " + response.statusCode() + 
                                 "\nResponse: " + response.body());
        }
        
        return responseType == Void.class ? null : objectMapper.readValue(response.body(), responseType);
    }
    
    //----- Generic Method To Send A PUT Request -----
    public static <T> T put(String endpoint, Object requestBody, Class<T> responseType) 
            throws IOException, InterruptedException {
        String jsonBody = requestBody != null ? objectMapper.writeValueAsString(requestBody) : null;
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""))
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json");
        
        //----- Add Authentication Token If Available -----
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (CONFIG.isLoggingEnabled()) {
            System.out.println("PUT " + BASE_URL + endpoint + " -> " + response.statusCode());
        }
        
        if (response.statusCode() >= 400) {
            throw new IOException("PUT request failed: " + response.statusCode() + 
                                 "\nResponse: " + response.body());
        }
        
        return responseType == Void.class ? null : objectMapper.readValue(response.body(), responseType);
    }
    
    //----- Generic Method To Send A DELETE Request -----
    public static <T> T delete(String endpoint, Class<T> responseType) 
            throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASE_URL + endpoint));
        
        //----- Add Authentication Token If Available -----
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (CONFIG.isLoggingEnabled()) {
            System.out.println("DELETE " + BASE_URL + endpoint + " -> " + response.statusCode());
        }
        
        if (response.statusCode() >= 400) {
            throw new IOException("DELETE request failed: " + response.statusCode() + 
                                 "\nResponse: " + response.body());
        }
        
        return responseType == Void.class ? null : objectMapper.readValue(response.body(), responseType);
    }
    
    //----- Convert An Object To A Map -----
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }
}
 