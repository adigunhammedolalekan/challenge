package com.starlingapp.roundup.models;

import java.util.List;

public record GetSavingsGoalResponse(
        List<SavingsGoal> savingsGoalList
) {}
