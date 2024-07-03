package com.starlingapp.roundup.services;

import com.starlingapp.roundup.integration.APIIntegrationService;
import com.starlingapp.roundup.models.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class AccountsServiceImpl implements AccountsService {

    private final APIIntegrationService apiIntegrationService;

    public AccountsServiceImpl(APIIntegrationService apiIntegrationService) {
        this.apiIntegrationService = apiIntegrationService;
    }

    @Override
    public Account getPrimaryAccount() {
        return apiIntegrationService.getAccounts()
                .stream()
                .filter(Account::isPrimary)
                .findFirst()
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "PRIMARY account not found for customer"));
    }
}
