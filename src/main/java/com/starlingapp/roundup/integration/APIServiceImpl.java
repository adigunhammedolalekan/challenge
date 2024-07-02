package com.starlingapp.roundup.integration;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;


public class APIServiceImpl implements APIService {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final String bearerToken;
    private final RestTemplate restTemplate;
    private final String baseURL;

    public APIServiceImpl(
            RestTemplate restTemplate,
            String baseURL,
            String bearerToken) {
        this.restTemplate = restTemplate;
        this.bearerToken = bearerToken;
        this.baseURL = baseURL;
    }

    @Override
    public <R, B> R put(String path, B body, Class<R> responseType) {
        return makeRequest(HttpMethod.PUT, path, body, responseType, Collections.emptyMap());
    }

    @Override
    public <R> R get(String path, Class<R> responseType) {
        return makeRequest(HttpMethod.GET, path, null, responseType, Collections.emptyMap());
    }

    @Override
    public <R> R getWithParams(String path, Map<String, String> queryParams, Class<R> responseType) {
        return makeRequest(HttpMethod.GET, path, null, responseType, queryParams);
    }

    private <R, B> R makeRequest(HttpMethod method, String path, B body, Class<R> responseType, Map<String, String> queryParams) {
        var url = baseURL + path;
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(AUTHORIZATION_HEADER, String.format("Bearer %s", bearerToken));
        return restTemplate.exchange(
                url,
                method,
                new HttpEntity<>(body, headers),
                responseType,
                queryParams).getBody();
    }
}
