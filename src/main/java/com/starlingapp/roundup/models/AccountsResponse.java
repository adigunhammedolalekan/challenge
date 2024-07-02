package com.starlingapp.roundup.models;

import java.util.List;

public record AccountsResponse(
        List<Account> accounts
) {}
