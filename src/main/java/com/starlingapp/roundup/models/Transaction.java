package com.starlingapp.roundup.models;

import com.starlingapp.roundup.models.enums.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.starlingapp.roundup.common.Utils.ONE_HUNDRED;
import static com.starlingapp.roundup.models.enums.SpendingCategory.SAVING;
import static com.starlingapp.roundup.models.enums.TransactionDirection.OUT;
import static com.starlingapp.roundup.models.enums.TransactionSource.INTERNAL_TRANSFER;
import static com.starlingapp.roundup.models.enums.TransactionStatus.SETTLED;
import static java.math.BigInteger.ZERO;

public record Transaction(

        UUID feedItemUid,

        UUID categoryUid,

        Money amount,

        Money sourceAmount,

        TransactionDirection direction,

        LocalDateTime updateAt,

        LocalDateTime transactionTime,

        LocalDateTime settlementTime,

        TransactionStatus status,

        UUID transactingApplicationUserUid,

        String reference,

        TransactionSource source,

        SpendingCategory spendingCategory
) {

    public BigInteger roundToNearestPound() {
        return BigDecimal.valueOf(
                amountInUnits().longValue())
                .divide(ONE_HUNDRED)
                .setScale(0, RoundingMode.UP)
                .multiply(ONE_HUNDRED)
                .toBigInteger();
    }

    public BigInteger amountInUnits() {
        return amount().minorUnits();
    }

    public boolean isEligibleForRounding() {
        if (spendingCategory == SAVING && source == INTERNAL_TRANSFER) {
            return false;
        }
        return direction == OUT
                && status == SETTLED
                && amountInUnits().compareTo(ZERO) > 0;
    }
}
