package com.starlingapp.roundup.models;

import java.time.LocalDate;

import static com.starlingapp.roundup.common.Utils.DEFAULT_SAVING_GOAL_NAME;
import static java.util.Objects.isNull;

public record RoundAndSaveRequest(
        String savingsGoalName,

        LocalDate since
) {

    @Override
    public String savingsGoalName() {
        return isNull(savingsGoalName) ? DEFAULT_SAVING_GOAL_NAME : savingsGoalName;
    }

    @Override
    public LocalDate since() {
        return isNull(since) ? LocalDate.EPOCH : since;
    }
}
