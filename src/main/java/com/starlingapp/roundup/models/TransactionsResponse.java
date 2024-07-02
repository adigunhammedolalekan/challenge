package com.starlingapp.roundup.models;

import java.util.List;

public record TransactionsResponse(
        List<Transaction> feedItems
) {}
