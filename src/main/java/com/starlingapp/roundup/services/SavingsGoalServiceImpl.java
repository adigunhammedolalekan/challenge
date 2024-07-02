package com.starlingapp.roundup.services;

import com.starlingapp.roundup.integration.APIIntegrationService;
import com.starlingapp.roundup.models.*;
import com.starlingapp.roundup.models.enums.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SavingsGoalServiceImpl implements SavingsGoalService {

    private final APIIntegrationService apiIntegrationService;
    private final Logger LOGGER = LoggerFactory.getLogger(SavingsGoalServiceImpl.class);

    public SavingsGoalServiceImpl(APIIntegrationService apiIntegrationService) {
        this.apiIntegrationService = apiIntegrationService;
    }

    @Override
    public SavingsGoal createFromTransactions(UUID accountUid, String savingsGoalName, List<Transaction> transactions) {
        var amount = transactions.stream()
                .map(transaction -> transaction
                        .roundToNearestPound()
                        .subtract(transaction.amountInUnits()))
                .reduce(BigInteger.ZERO, BigInteger::add);
        var savingsGoal = getOrCreate(accountUid, savingsGoalName);


        apiIntegrationService.addMoneyToSavingsGoal(accountUid, savingsGoal.savingsGoalUid(),
                SavingsGoalTransferRequest.of(Currency.GBP, amount));

        LOGGER.info("Successfully saved {} to savingsGoal {}:{} ",
                amount,
                savingsGoal,
                savingsGoal.savingsGoalUid());

        return savingsGoal;
    }

    @Override
    public SavingsGoal getOrCreate(UUID accountUid, String savingsGoalName) {
        return getSavingsGoalByName(accountUid, savingsGoalName)
                .or(() -> apiIntegrationService.createSavingsGoal(accountUid, CreateSavingGoalRequest.of(savingsGoalName, Currency.GBP)))
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private Optional<SavingsGoal> getSavingsGoalByName(UUID accountUid, String savingsGoalName) {
        return apiIntegrationService.getSavingsGoalsForAccount(accountUid)
                .stream()
                .filter(SavingsGoal::isActive)
                .filter(savingsGoal -> Objects.equals(savingsGoal.name(), savingsGoalName))
                .findFirst();
    }
}
