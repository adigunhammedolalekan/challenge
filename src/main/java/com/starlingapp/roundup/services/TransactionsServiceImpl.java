package com.starlingapp.roundup.services;

import com.starlingapp.roundup.integration.APIIntegrationService;
import com.starlingapp.roundup.models.Account;
import com.starlingapp.roundup.models.Transaction;

import java.time.LocalDate;
import java.util.List;

public class TransactionsServiceImpl implements TransactionsService {

    private final APIIntegrationService apiIntegrationService;

    public TransactionsServiceImpl(APIIntegrationService apiIntegrationService) {
        this.apiIntegrationService = apiIntegrationService;
    }

    @Override
    public List<Transaction> getEligibleTransactions(Account account, LocalDate since) {
        return apiIntegrationService
                .getTransactionsForAccount(account.accountUid(), account.defaultCategory(), since)
                .stream()
                .filter(Transaction::isEligibleForRounding)
                .toList();
    }
}
