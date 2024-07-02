package com.starlingapp.roundup.integration;

import com.starlingapp.roundup.models.GetSavingsGoalResponse;
import com.starlingapp.roundup.models.SavingsGoalTransferRequest;
import com.starlingapp.roundup.models.SavingsGoalTransferResponse;
import com.starlingapp.roundup.models.TransactionsResponse;
import com.starlingapp.roundup.models.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class APIServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private APIService apiService;
    private static final String mockBearerToken = "1baf84bd-7fb0-4839-8258-2ef4b4fab2ec";
    private static final String mockBaseUrl = "https://api.mock.service/";

    @BeforeEach()
    protected void setUp() {
        apiService = new APIServiceImpl(restTemplate, mockBaseUrl, mockBearerToken);
    }

    @Test
    void put_test() {
        var expectedURL = mockBaseUrl + "transfers";
        var body = SavingsGoalTransferRequest.of(Currency.GBP, BigInteger.valueOf(100));
        var expectedHeaders = getHeaders();
        var expectedHttpEntity = new HttpEntity<>(body, expectedHeaders);
        var expectedResponse = new SavingsGoalTransferResponse(UUID.randomUUID());

        when(restTemplate.exchange(
                expectedURL,
                HttpMethod.PUT,
                expectedHttpEntity,
                SavingsGoalTransferResponse.class,
                Collections.emptyMap()
        )).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        var response = apiService.put("transfers", body, SavingsGoalTransferResponse.class);

        assertEquals(expectedResponse, response);
    }

    @Test
    void get_test() {
        var expectedURL = mockBaseUrl + "savings-goal";
        var expectedHeaders = getHeaders();
        var expectedHttpEntity = new HttpEntity<>(null, expectedHeaders);
        var expectedResponse = new GetSavingsGoalResponse(Collections.emptyList());

        when(restTemplate.exchange(
                expectedURL,
                HttpMethod.GET,
                expectedHttpEntity,
                GetSavingsGoalResponse.class,
                Collections.emptyMap()
        )).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        var response = apiService.get("savings-goal", GetSavingsGoalResponse.class);

        assertEquals(expectedResponse, response);
    }

    @Test
    void get_with_params_test() {
        var expectedURL = mockBaseUrl + "transactions?accountUid={accountUid}&page={page}";
        var expectedHeaders = getHeaders();
        var expectedHttpEntity = new HttpEntity<>(null, expectedHeaders);
        var expectedResponse = new TransactionsResponse(Collections.emptyList());
        var queryParams = Map.of("accountUid", UUID.randomUUID().toString(), "page", "10");

        when(restTemplate.exchange(
                expectedURL,
                HttpMethod.GET,
                expectedHttpEntity,
                TransactionsResponse.class,
                queryParams
        )).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        var response = apiService.getWithParams("transactions?accountUid={accountUid}&page={page}",
                queryParams,
                TransactionsResponse.class);

        assertEquals(expectedResponse, response);
    }

    private HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + mockBearerToken);
        return headers;
    }
}
