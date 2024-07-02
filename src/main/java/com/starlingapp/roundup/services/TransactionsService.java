package com.starlingapp.roundup.services;

import com.starlingapp.roundup.models.Account;
import com.starlingapp.roundup.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionsService {

    List<Transaction> getEligibleTransactions(Account account, LocalDate since);
}
