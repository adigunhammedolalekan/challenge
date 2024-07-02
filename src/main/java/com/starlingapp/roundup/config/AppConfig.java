package com.starlingapp.roundup.config;

import com.starlingapp.roundup.integration.APIIntegrationService;
import com.starlingapp.roundup.integration.APIIntegrationServiceImpl;
import com.starlingapp.roundup.integration.APIService;
import com.starlingapp.roundup.integration.APIServiceImpl;
import com.starlingapp.roundup.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    APIService apiService(RestTemplate restTemplate,
                          @Value("${integrations.starling.baseUrl}") String baseUrl,
                          @Value("${integrations.starling.bearerToken}") String bearerToken) {
        return new APIServiceImpl(restTemplate, baseUrl, bearerToken);
    }

    @Bean
    APIIntegrationService apiIntegrationService(APIService apiService) {
        return new APIIntegrationServiceImpl(apiService);
    }

    @Bean
    AccountsService accountsService(
            APIIntegrationService apiIntegrationService) {
        return new AccountsServiceImpl(apiIntegrationService);
    }

    @Bean
    TransactionsService transactionsService(
            APIIntegrationService apiIntegrationService) {
        return new TransactionsServiceImpl(apiIntegrationService);
    }

    @Bean
    SavingsGoalService savingsGoalService(
            APIIntegrationService apiIntegrationService) {
        return new SavingsGoalServiceImpl(apiIntegrationService);
    }

    @Bean
    RoundingService roundingService(
            AccountsService accountsService,
            TransactionsService transactionsService,
            SavingsGoalService savingsGoalService) {
        return new RoundingServiceImpl(accountsService, transactionsService, savingsGoalService);
    }
}
