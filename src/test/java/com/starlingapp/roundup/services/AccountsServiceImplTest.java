package com.starlingapp.roundup.services;

import com.starlingapp.roundup.integration.APIIntegrationService;
import com.starlingapp.roundup.models.Account;
import com.starlingapp.roundup.models.enums.AccountType;
import com.starlingapp.roundup.models.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountsServiceImplTest {

    @Mock
    private APIIntegrationService apiIntegrationService;

    private AccountsService accountsService;

    @BeforeEach
    public void setUp() {
        accountsService = new AccountsServiceImpl(apiIntegrationService);
    }

    @Test
    void get_primary_account_test() {
        var primaryAccount = new Account(UUID.randomUUID(), AccountType.PRIMARY, UUID.randomUUID(), Currency.GBP, null, null);
        var accounts = List.of(
                primaryAccount,
                new Account(UUID.randomUUID(), AccountType.LOAN, UUID.randomUUID(), Currency.GBP, null, null));

        when(apiIntegrationService.getAccounts()).thenReturn(accounts);

        var response = accountsService.getPrimaryAccount();

        assertEquals(primaryAccount, response);
    }
}
