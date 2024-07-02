package com.starlingapp.roundup.integration;

import com.starlingapp.roundup.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientResponseException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class APIIntegrationServiceImpl implements APIIntegrationService {

    private final Logger LOGGER = LoggerFactory.getLogger(APIIntegrationServiceImpl.class);

    private final APIService apiService;
    private static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    public APIIntegrationServiceImpl(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public List<Account> getAccounts() {
        try {
            return apiService.get("accounts", AccountsResponse.class)
                    .accounts();
        } catch (RestClientResponseException exception) {
            LOGGER.error("failed to GET /accounts: {}", exception.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Transaction> getTransactionsForAccount(
            UUID accountUid,
            UUID categoryUid,
            LocalDate changesSince) {
        var path = String.format("feed/account/%s/category/%s?changesSince={changesSince}", accountUid, categoryUid);
        var queryParams = Map.of("changesSince", formatDateUTC(changesSince));
        try {
            return apiService.getWithParams(path, queryParams, TransactionsResponse.class)
                    .feedItems();
        } catch (RestClientResponseException exception) {
            LOGGER.error("failed to GET {}: {}, params={}", path, exception.getMessage(), queryParams);
            return List.of();
        }
    }

    @Override
    public List<SavingsGoal> getSavingsGoalsForAccount(UUID accountUid) {
        var path = String.format("account/%s/savings-goals", accountUid);
        try {
            return apiService.get(path, GetSavingsGoalResponse.class)
                    .savingsGoalList();
        } catch (RestClientResponseException exception) {
            LOGGER.error("failed to GET {}: {}", path, exception.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<SavingsGoal> createSavingsGoal(UUID accountUid, CreateSavingGoalRequest savingsGoal) {
        var path = String.format("account/%s/savings-goals", accountUid);
        try {
            return Optional.ofNullable(
                    apiService.put(path, savingsGoal, SavingsGoal.class));
        } catch (RestClientResponseException exception) {
            LOGGER.error("failed to PUT {}: {}", path, exception.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<SavingsGoalTransferResponse> addMoneyToSavingsGoal(UUID accountUid,
                                                                       UUID savingsGoalUid,
                                                                       SavingsGoalTransferRequest request) {
        var path = String.format("account/%s/savings-goals/%s/add-money/%s",
                accountUid, savingsGoalUid, request.transferUid());
        try {
            return Optional.ofNullable(apiService
                    .put(path, request, SavingsGoalTransferResponse.class));
        } catch (RestClientResponseException exception) {
            LOGGER.error("failed to PUT {}: {}: {}", path, exception.getMessage(), request);
            return Optional.empty();
        }
    }

    private String formatDateUTC(LocalDate localDate) {
        return localDate
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(API_DATE_FORMAT));
    }
}
