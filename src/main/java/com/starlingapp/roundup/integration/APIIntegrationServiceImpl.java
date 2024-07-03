package com.starlingapp.roundup.integration;

import com.starlingapp.roundup.models.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class APIIntegrationServiceImpl implements APIIntegrationService {

    private final APIService apiService;
    private static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    public APIIntegrationServiceImpl(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public List<Account> getAccounts() {
        return apiService.get("accounts", AccountsResponse.class)
                .accounts();
    }

    @Override
    public List<Transaction> getTransactionsForAccount(
            UUID accountUid,
            UUID categoryUid,
            LocalDate changesSince) {
        var path = String.format("feed/account/%s/category/%s?changesSince={changesSince}", accountUid, categoryUid);
        var queryParams = Map.of("changesSince", formatDateUTC(changesSince));
        return apiService.getWithParams(path, queryParams, TransactionsResponse.class)
                .feedItems();
    }

    @Override
    public List<SavingsGoal> getSavingsGoalsForAccount(UUID accountUid) {
        var path = String.format("account/%s/savings-goals", accountUid);
        return apiService.get(path, GetSavingsGoalResponse.class)
                .savingsGoalList();
    }

    @Override
    public Optional<SavingsGoal> createSavingsGoal(UUID accountUid, CreateSavingGoalRequest savingsGoal) {
        var path = String.format("account/%s/savings-goals", accountUid);
        return Optional.ofNullable(
                apiService.put(path, savingsGoal, SavingsGoal.class));
    }

    @Override
    public Optional<SavingsGoalTransferResponse> addMoneyToSavingsGoal(UUID accountUid,
                                                                       UUID savingsGoalUid,
                                                                       SavingsGoalTransferRequest request) {
        var path = String.format("account/%s/savings-goals/%s/add-money/%s",
                accountUid, savingsGoalUid, request.transferUid());
        return Optional.ofNullable(apiService
                .put(path, request, SavingsGoalTransferResponse.class));
    }

    private String formatDateUTC(LocalDate localDate) {
        return localDate
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(API_DATE_FORMAT));
    }
}
