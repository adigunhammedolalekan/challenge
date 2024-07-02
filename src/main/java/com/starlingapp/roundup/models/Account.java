package com.starlingapp.roundup.models;

import com.starlingapp.roundup.models.enums.AccountType;
import com.starlingapp.roundup.models.enums.Currency;

import java.time.LocalDateTime;
import java.util.UUID;

public record Account(
        UUID accountUid,

        AccountType accountType,

        UUID defaultCategory,

        Currency currency,

        LocalDateTime createdAt,

        String name
) {

    public boolean isPrimary() {
        return accountType == AccountType.PRIMARY;
    }
}
