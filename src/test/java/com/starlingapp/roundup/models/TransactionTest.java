package com.starlingapp.roundup.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static com.starlingapp.roundup.TestUtils.createTransaction;
import static com.starlingapp.roundup.models.enums.SpendingCategory.*;
import static com.starlingapp.roundup.models.enums.TransactionDirection.IN;
import static com.starlingapp.roundup.models.enums.TransactionDirection.OUT;
import static com.starlingapp.roundup.models.enums.TransactionSource.*;
import static com.starlingapp.roundup.models.enums.TransactionStatus.PENDING;
import static com.starlingapp.roundup.models.enums.TransactionStatus.SETTLED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

    @Test
    void round_to_nearest_pound_test() {
        record testTransaction(
                Transaction transaction,
                BigInteger expected
        ) {}

        var testTransactions = List.of(
                new testTransaction(createTransaction(435, SETTLED, OUT, SAVING, CARD_FEE_CHARGE), BigInteger.valueOf(500)),
                new testTransaction(createTransaction(520, SETTLED, IN, SAVING, CARD_FEE_CHARGE), BigInteger.valueOf(600)),
                new testTransaction(createTransaction(87, PENDING, OUT, SAVING, SEPA_DIRECT_DEBIT), BigInteger.valueOf(100)),
                new testTransaction(createTransaction(4356, SETTLED, OUT, CAR, CARD_FEE_CHARGE), BigInteger.valueOf(4400)));


        for (var next : testTransactions) {
            var nearestPound = next.transaction().roundToNearestPound();
            assertEquals(nearestPound, next.expected());
        }
    }

    @Test
    void is_eligible_for_rounding_test() {
        record testTransaction(
                Transaction transaction,
                boolean expected
        ) {}

        var testTransactions = List.of(
                new testTransaction(createTransaction(100, SETTLED, OUT, CAR, CASH_WITHDRAWAL), true),
                new testTransaction(createTransaction(100, PENDING, OUT, CAR, CASH_WITHDRAWAL), false),
                new testTransaction(createTransaction(100, SETTLED, IN, CAR, SEPA_CREDIT_TRANSFER), false),
                new testTransaction(createTransaction(100, SETTLED, OUT, SAVING, CASH_WITHDRAWAL), true),
                new testTransaction(createTransaction(100, SETTLED, OUT, SAVING, INTERNAL_TRANSFER), false)
        );

        for (var next : testTransactions) {
            assertEquals(next.transaction().isEligibleForRounding(), next.expected);
        }
    }
}
