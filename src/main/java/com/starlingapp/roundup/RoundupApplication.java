package com.starlingapp.roundup;

import com.starlingapp.roundup.integration.APIIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = {"com.starlingapp.roundup.*"})
public class RoundupApplication implements CommandLineRunner {

	@Autowired
	private APIIntegrationService apiIntegrationService;

	private final Logger LOGGER = LoggerFactory.getLogger(RoundupApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RoundupApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var accounts = apiIntegrationService.getAccounts();
		LOGGER.info("Accounts: {}", accounts);

		var transactions = apiIntegrationService.getTransactionsForAccount(
				accounts.getFirst().accountUid(),
				accounts.getFirst().defaultCategory(),
				LocalDate.of(1970, 1, 1));

		LOGGER.info("Transactions: {}", transactions);

		var savingGoals = apiIntegrationService.getSavingsGoalsForAccount(accounts.getFirst().accountUid())
				.stream()
				.findFirst()
				.orElseThrow();

		LOGGER.info("Saving goals: {}", savingGoals);

		/*var addMoneyResponse = apiIntegrationService.addMoneyToSavingsGoal(
				accounts.getFirst().accountUid(),
				savingGoals.savingsGoalUid(),
				new SavingsGoalTransferRequest(
						new Money(Currency.GBP, BigInteger.valueOf(100)),
						UUID.randomUUID()));
		LOGGER.info("Add money: {}", addMoneyResponse);*/
	}
}
