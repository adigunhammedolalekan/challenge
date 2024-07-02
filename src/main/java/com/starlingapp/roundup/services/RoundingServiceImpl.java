package com.starlingapp.roundup.services;

import com.starlingapp.roundup.models.RoundAndSaveRequest;
import com.starlingapp.roundup.models.SavingsGoal;

public class RoundingServiceImpl implements RoundingService {

    private final AccountsService accountsService;
    private final TransactionsService transactionsService;
    private final SavingsGoalService savingsGoalService;

    public RoundingServiceImpl(
            AccountsService accountsService,
            TransactionsService transactionsService,
            SavingsGoalService savingsGoalService) {
        this.accountsService = accountsService;
        this.transactionsService = transactionsService;
        this.savingsGoalService = savingsGoalService;
    }

    @Override
    public SavingsGoal roundAndSave(RoundAndSaveRequest request) {
        var account = accountsService.getPrimaryAccount();
        return savingsGoalService.createFromTransactions(
                account.accountUid(),
                request.savingsGoalName(),
                transactionsService.getEligibleTransactions(account, request.since()));
    }
}
