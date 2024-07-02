package com.starlingapp.roundup.models;

import com.starlingapp.roundup.models.enums.Currency;

import java.math.BigInteger;

public record Money(
        Currency currency,

        BigInteger minorUnits
) {}
