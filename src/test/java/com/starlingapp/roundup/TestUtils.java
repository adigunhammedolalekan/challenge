package com.starlingapp.roundup;

import com.starlingapp.roundup.models.Money;
import com.starlingapp.roundup.models.Transaction;
import com.starlingapp.roundup.models.enums.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestUtils {

    public static Transaction createTransaction(
            int amount,
            TransactionStatus status,
            TransactionDirection direction,
            SpendingCategory category,
            TransactionSource source) {
        return new Transaction(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(Currency.GBP, BigInteger.valueOf(amount)),
                new Money(Currency.GBP, BigInteger.valueOf(amount)),
                direction,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                status,
                UUID.randomUUID(),
                "Aldi: REF001",
                source,
                category);
    }
}
