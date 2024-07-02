package com.starlingapp.roundup.models;

import com.starlingapp.roundup.models.enums.Currency;

import java.math.BigInteger;
import java.util.UUID;

public record SavingsGoalTransferRequest(
        Money amount,

        UUID transferUid
) {

    public static SavingsGoalTransferRequest of(Currency currency, BigInteger amount) {
        return new SavingsGoalTransferRequest(
                new Money(currency, amount),
                UUID.randomUUID());
    }
}
