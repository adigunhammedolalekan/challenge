package com.starlingapp.roundup.models;

import com.starlingapp.roundup.models.enums.Currency;
import jakarta.validation.constraints.NotNull;

public record CreateSavingGoalRequest(
        @NotNull
        String name,

        @NotNull
        Currency currency,

        Money target
) {

        public static CreateSavingGoalRequest of(String name, Currency currency) {
                return new CreateSavingGoalRequest(name, currency, null);
        }
}
