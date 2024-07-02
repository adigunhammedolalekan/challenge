package com.starlingapp.roundup.services;

import com.starlingapp.roundup.TestUtils;
import com.starlingapp.roundup.models.Account;
import com.starlingapp.roundup.models.RoundAndSaveRequest;
import com.starlingapp.roundup.models.SavingsGoal;
import com.starlingapp.roundup.models.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.starlingapp.roundup.TestUtils.createTransaction;
import static com.starlingapp.roundup.models.enums.SpendingCategory.CASH;
import static com.starlingapp.roundup.models.enums.TransactionDirection.OUT;
import static com.starlingapp.roundup.models.enums.TransactionSource.CASH_WITHDRAWAL;
import static com.starlingapp.roundup.models.enums.TransactionStatus.SETTLED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoundingServiceImplTest {

    @Mock
    private AccountsService accountsService;

    @Mock
    private TransactionsService transactionsService;

    @Mock
    private SavingsGoalService savingsGoalService;

    private RoundingService roundingService;

    @BeforeEach
    public void setUp() {
        roundingService = new RoundingServiceImpl(accountsService, transactionsService, savingsGoalService);
    }

    @Test
    void round_and_save_test() {
        var account = new Account(UUID.randomUUID(), AccountType.PRIMARY, UUID.randomUUID(),
                Currency.GBP, LocalDateTime.now(), "Hammed Adigun");
        var since = LocalDate.of(2024, 6, 1);
        var request = new RoundAndSaveRequest("Hammed Holiday", since);
        var transactions = List.of(createTransaction(190, SETTLED, OUT, CASH, CASH_WITHDRAWAL));
        var expectedSavingsGoal = new SavingsGoal(UUID.randomUUID(), "Hammed Holiday", SavingsGoalState.ACTIVE, null, null, null);

        when(accountsService.getPrimaryAccount()).thenReturn(account);
        when(transactionsService.getEligibleTransactions(account, since))
                .thenReturn(transactions);
        when(savingsGoalService.createFromTransactions(account.accountUid(),
                "Hammed Holiday", transactions)).thenReturn(expectedSavingsGoal);

        var response = roundingService.roundAndSave(request);

        assertEquals(response, expectedSavingsGoal);
    }
}
