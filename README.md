### Starling Bank Challenge

For a customer, take all the transactions in a given week and round them up to the nearest
pound. For example with spending of £4.35, £5.20 and £0.87, the round-up would be £1.58.
This amount should then be transferred into a savings goal, helping the customer save for
future adventures.

### Running the app
```bash
export STARLING_API_BASEURL=https://api-sandbox.starlingbank.com/api/v2/ STARTLING_API_BEARER_TOKEN={yourApplicationBearerToken} && ./gradlew bootRun
```

### Running with Docker
```bash
# run build
./gradlew clean build

# build image
 docker build -t roundup-app .
 
 # run
 docker run -p 8080:8080 -d -e STARLING_API_BASEURL='https://api-sandbox.starlingbank.com/api/v2/' -e STARLING_API_BEARER_TOKEN='yourApplicationBearerToken' roundup-app
```

### Testing
```curl
curl -X PUT \
  http://localhost:8080/round-save \
  -H 'Content-Type: application/json' \
  -d '{"savingsGoalName": "Buy a car", "since": "2024-06-25"}'
```

## How it works
#### Flow Diagram
![Flow Diagram](https://github.com/adigunhammedolalekan/starling-challenge/blob/main/flows/roundup_flows.png)

* Client make an API call to the endpoint `PUT /round-save`
* ```json
  PUT /round-save
  {
      "savingsGoalName": "Hammed Holiday",
      "since": "2024-06-20
  }
  ```
  ```json
  // Sample Response
  {
    "savingsGoalUid": "62ad6b8f-f00b-44c8-892f-3d2ea0d44c8e",
    "name": "Hammed Savings",
    "state": "ACTIVE",
    "target": null,
    "totalSaved": {
        "currency": "GBP",
        "minorUnits": 800
    },
    "savedPercentage": null,
    "active": true
  }
  ```
### Request description
  * **savingsGoalName**: Optional - the name of the SavingsGoal client wishes to save the rounded amount. Defaults to `Hammed Savings` if null is given
  * **since**: Optional - Date from when transactions rounding should start. Defaults to 1week ago if null is given

### How it works - Breakdown
* **RoundingController** receives the request and pass it to RoundingService
* **RoundingService**:
  * fetch primary account using AccountsService + APIIntegrationService
  * fetch transactions using TransactionsService + APIIntegrationService 
    * **TransactionsService** uses the parameters from the request and the customer account details to fetch transactions and then filter out ineligible transactions using the following criteria
        * direction = OUT
        * status = SETTLED
        * amount > 0
        * source != INTERNAL_TRANSFERS
        * spendingCategory != SAVING
  
    
  filtering out the ineligible transactions ensures we only round transactions that are a result of outbound payments, are completed and not a transaction generated by transferring to the same or another SavingsGoal 
  * call SavingsGoalService passing the customer account, the savingsGoalName and the eligible transactions
* **SavingsGoalService**:
  * calculate the total rounded amount from the eligible transactions
  * create or get a SavingsGoal - this is done by fetching all existing SavingsGoal for the customer and then finding the given SavingsGoal by name. If non is found, we create a new SavingsGoal using the given name, otherwise, no savingsGoalName was given so we use the default 'Hammed Savings'.
  * add money to the SavingsGoal created/retrieved from the step above

### Potential Improvements
* **Authentication and Authorisation** - Currently, the app uses a pre-configured bearer token and automatically selects the primary account associated with this token. To improve security and expand functionality, the app should allow the bearer token to be passed as an Authorization header in the PUT /round-save endpoint. This change will not only enhance security but also extend the API to support multiple customers, rather than just the single owner of the pre-configured token.
* **Keeping track of rounded transactions** - Currently, the app processes transactions from one week ago (or a custom date) and rounds them, regardless of whether they have been rounded before. An improvement would be to track rounded transactions to ensure they are not rounded again.