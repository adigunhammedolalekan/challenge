package com.starlingapp.roundup.integration;

import com.starlingapp.roundup.models.*;
import com.starlingapp.roundup.models.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class APIIntegrationServiceImplTest {

    @Mock
    private APIService apiService;

    private APIIntegrationService apiIntegrationService;

    @BeforeEach()
    public void setUp() {
        apiIntegrationService = new APIIntegrationServiceImpl(apiService);
    }

    @Test
    void get_accounts_test() {
        var expectedResponse = new AccountsResponse(
                List.of(new Account(UUID.randomUUID(), AccountType.PRIMARY, UUID.randomUUID(), Currency.GBP, LocalDateTime.MAX, "Hammed")));

        when(apiService.get("accounts", AccountsResponse.class))
                .thenReturn(expectedResponse);

        var actualResponse = apiIntegrationService.getAccounts();

        assertEquals(expectedResponse.accounts(), actualResponse);
    }

    @Test
    void get_transactions_for_account_test() {
        var accountUid = UUID.fromString("960d57a5-31aa-4791-b82e-ad2154bfb266");
        var categoryUid = UUID.fromString("e14d38a0-c011-4383-b641-a01b6456e714");
        var expectedResponse = new TransactionsResponse(
                List.of(getMockTransaction()));
        var changesSince = LocalDate.of(2020, 1, 1);
        var expectedQueryParams = Map.of("changesSince", "2020-01-01T00:00:00.000Z");
        var fullPath = "feed/account/960d57a5-31aa-4791-b82e-ad2154bfb266/category/e14d38a0-c011-4383-b641-a01b6456e714?changesSince={changesSince}";

        when(apiService.getWithParams(fullPath, expectedQueryParams, TransactionsResponse.class))
                .thenReturn(expectedResponse);

        var response = apiIntegrationService.getTransactionsForAccount(accountUid, categoryUid, changesSince);

        assertEquals(expectedResponse.feedItems(), response);
    }

    @Test
    void get_savings_goal_for_account_test() {
        var accountUid = UUID.fromString("960d57a5-31aa-4791-b82e-ad2154bfb266");
        var expectedResponse = new GetSavingsGoalResponse(
                List.of(new SavingsGoal(UUID.randomUUID(), "TEST", SavingsGoalState.ACTIVE, null, null, null)));
        var expectedPath = "account/960d57a5-31aa-4791-b82e-ad2154bfb266/savings-goals";
        when(apiService.get(expectedPath, GetSavingsGoalResponse.class))
                .thenReturn(expectedResponse);

        var response = apiIntegrationService.getSavingsGoalsForAccount(accountUid);

        assertEquals(expectedResponse.savingsGoalList(), response);
    }

    @Test
    void create_savings_goal_test() {
        var accountUid = UUID.fromString("960d57a5-31aa-4791-b82e-ad2154bfb266");
        var expectedPath = "account/960d57a5-31aa-4791-b82e-ad2154bfb266/savings-goals";
        var request = new CreateSavingGoalRequest("TEST", Currency.GBP, null);
        var expectedResponse = new SavingsGoal(UUID.randomUUID(), "TEST", SavingsGoalState.ACTIVE, null, null, null);

        when(apiService.put(expectedPath, request, SavingsGoal.class))
                .thenReturn(expectedResponse);

        var response = apiIntegrationService.createSavingsGoal(accountUid, request);

        assertTrue(response.isPresent());
        assertEquals(expectedResponse, response.get());
    }

    @Test
    void add_money_to_savings_goal_test() {
        var accountUid = UUID.fromString("960d57a5-31aa-4791-b82e-ad2154bfb266");
        var savingsGoalUid = UUID.fromString("56daa976-e9f0-47e8-9b3c-d77ae718c3a9");
        var transferUid = UUID.fromString("f6b8cf6b-5523-4739-9736-1281a6a44449");
        var transferRequest = new SavingsGoalTransferRequest(
                new Money(Currency.GBP, BigInteger.valueOf(1000)), transferUid);
        var transferResponse = new SavingsGoalTransferResponse(transferUid);
        var fullPath = "account/960d57a5-31aa-4791-b82e-ad2154bfb266/savings-goals/56daa976-e9f0-47e8-9b3c-d77ae718c3a9/add-money/f6b8cf6b-5523-4739-9736-1281a6a44449";

        when(apiService.put(fullPath, transferRequest, SavingsGoalTransferResponse.class))
                .thenReturn(transferResponse);

        var response = apiIntegrationService.addMoneyToSavingsGoal(accountUid, savingsGoalUid, transferRequest);

        assertTrue(response.isPresent());
        assertEquals(transferResponse, response.get());
    }

    private RestClientResponseException getMockException() {
        return new RestClientResponseException(
                "failed",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "failed to complete request",
                null,
                null,
                null);
    }

    private Transaction getMockTransaction() {
        return new Transaction(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(Currency.GBP, BigInteger.TWO),
                new Money(Currency.GBP, BigInteger.TEN),
                TransactionDirection.OUT,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                TransactionStatus.SETTLED,
                UUID.randomUUID(),
                "Nandos: REF001",
                TransactionSource.MASTER_CARD,
                SpendingCategory.EATING_OUT);
    }
}
