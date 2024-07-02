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

### How it works
