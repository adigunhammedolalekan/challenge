package com.starlingapp.roundup.models;

import com.starlingapp.roundup.models.enums.SavingsGoalState;

import java.math.BigDecimal;
import java.util.UUID;

import static com.starlingapp.roundup.models.enums.SavingsGoalState.ACTIVE;

public record SavingsGoal(
        UUID savingsGoalUid,

        String name,

        SavingsGoalState state,

        Money target,

        Money totalSaved,

        BigDecimal savedPercentage
) {

    public boolean isActive() {
        return ACTIVE == state;
    }
}
