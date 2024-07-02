package com.starlingapp.roundup.integration;

import com.starlingapp.roundup.models.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface APIIntegrationService {
    List<Account> getAccounts();

    List<Transaction> getTransactionsForAccount(
            UUID accountUid,
            UUID categoryUid,
            LocalDate changesSince);

    List<SavingsGoal> getSavingsGoalsForAccount(UUID accountUid);

    Optional<SavingsGoal> createSavingsGoal(
            UUID accountUid,
            CreateSavingGoalRequest savingsGoal);

    Optional<SavingsGoalTransferResponse> addMoneyToSavingsGoal(
            UUID accountUid,
            UUID savingsGoalId,
            SavingsGoalTransferRequest request);
}
