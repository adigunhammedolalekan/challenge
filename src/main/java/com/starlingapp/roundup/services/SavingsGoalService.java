package com.starlingapp.roundup.services;

import com.starlingapp.roundup.models.SavingsGoal;
import com.starlingapp.roundup.models.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavingsGoalService {

    SavingsGoal createFromTransactions(
            UUID accountUid,
            String savingsGoalName,
            List<Transaction> transactions);

    SavingsGoal getOrCreate(UUID accountUid, String savingsGoalName);
}
