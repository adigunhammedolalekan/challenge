package com.starlingapp.roundup.services;

import com.starlingapp.roundup.integration.APIIntegrationService;
import com.starlingapp.roundup.models.*;
import com.starlingapp.roundup.models.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.starlingapp.roundup.TestUtils.createTransaction;
import static com.starlingapp.roundup.models.enums.SpendingCategory.CASH;
import static com.starlingapp.roundup.models.enums.TransactionDirection.OUT;
import static com.starlingapp.roundup.models.enums.TransactionSource.CASH_WITHDRAWAL;
import static com.starlingapp.roundup.models.enums.TransactionStatus.SETTLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SavingsGoalServiceImplTest {

    @Mock
    private APIIntegrationService apiIntegrationService;

    private SavingsGoalService savingsGoalService;

    @BeforeEach
    public void setUp() {
        savingsGoalService = new SavingsGoalServiceImpl(apiIntegrationService);
    }

    @Test
    void create_savings_goal_and_transfer_money_to_savings_goal_test() {
        var accountUid = UUID.randomUUID();
        var transferUid = UUID.randomUUID();
        var mockSavingsGoalName = "Hammed Holiday";
        var savingsGoals = mockSavingsGoals(mockSavingsGoalName);
        var expectedSavingsGoal = savingsGoals.getFirst();
        var transactions = List.of(
                createTransaction(435, SETTLED, OUT, CASH, CASH_WITHDRAWAL),
                createTransaction(520, SETTLED, OUT, CASH, CASH_WITHDRAWAL),
                createTransaction(87, SETTLED, OUT, CASH, CASH_WITHDRAWAL));
        var expectedTransferRequest = new SavingsGoalTransferRequest(new Money(Currency.GBP, BigInteger.valueOf(158)), transferUid);
        var argumentCaptor = ArgumentCaptor.forClass(SavingsGoalTransferRequest.class);

        when(apiIntegrationService.getSavingsGoalsForAccount(accountUid))
                .thenReturn(savingsGoals);
        when(apiIntegrationService.addMoneyToSavingsGoal(eq(accountUid), eq(expectedSavingsGoal.savingsGoalUid()), argumentCaptor.capture()))
                .thenReturn(Optional.of(new SavingsGoalTransferResponse(transferUid)));

        var response = savingsGoalService.createFromTransactions(
                accountUid,
                mockSavingsGoalName,
                transactions);

        assertEquals(response, expectedSavingsGoal);
        assertThat(argumentCaptor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("transferUid")
                .isEqualTo(expectedTransferRequest);
    }

    @Test
    void get_or_create_savings_goal_test() {
        var accountUid = UUID.randomUUID();
        var savingsGoals = mockSavingsGoals("Hammed Holiday");
        var expectedSavingsGoal = savingsGoals.getFirst();

        when(apiIntegrationService.getSavingsGoalsForAccount(accountUid))
                .thenReturn(savingsGoals);

        var response = savingsGoalService.getOrCreate(accountUid, "Hammed Holiday");

        assertEquals(response, expectedSavingsGoal);
    }

    @Test
    void get_or_create_savings_goal_create_default_test() {
        var accountUid = UUID.randomUUID();
        var newSavingsGoalName = "Buy a car";
        var expectedSavingsGoal = new SavingsGoal(UUID.randomUUID(), newSavingsGoalName, SavingsGoalState.ACTIVE, null, null, null);
        var createSavingsGoalRequest = new CreateSavingGoalRequest(newSavingsGoalName, Currency.GBP, null);

        when(apiIntegrationService.getSavingsGoalsForAccount(accountUid))
                .thenReturn(mockSavingsGoals("Hammed Holiday"));
        when(apiIntegrationService.createSavingsGoal(accountUid, createSavingsGoalRequest))
                .thenReturn(Optional.of(expectedSavingsGoal));

        var response = savingsGoalService.getOrCreate(accountUid, newSavingsGoalName);

        assertEquals(response, expectedSavingsGoal);
    }

    private List<SavingsGoal> mockSavingsGoals(String existingSavingsGoalName) {
        return List.of(
                new SavingsGoal(UUID.randomUUID(), existingSavingsGoalName, SavingsGoalState.ACTIVE, null, null, null),
                new SavingsGoal(UUID.randomUUID(), "Buy a house", SavingsGoalState.ACTIVE, null, null, null),
                new SavingsGoal(UUID.randomUUID(), "Weddings", SavingsGoalState.ACTIVE, null, null, null));
    }
}
