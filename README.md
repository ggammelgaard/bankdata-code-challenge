# Bankdata Code Challenge

Author: Gustav A. Gammelgaard

## Examples of application
See the folder `.\ìmages\`

## Starting the application

Start the backend with the command 
```bash  
.\mvnw compile quarkus:dev
```

Start the frontend with the commands
```bash  
cd .\src\main\webapp
npm install
npm start
```

## Interacting with the application
Frontend is served on `http://localhost:3000/`

Backend OpenAPI specification can be found on `http://localhost:8080/swagger`

### Curl commands:
1.1 - Create a new Account: 
```bash  
curl -X 'POST' \
  'http://localhost:8080/account?balance=200&firstName=Lars&lastName=Larsen' \
  -H 'accept: */*' \
  -d ''
```

1.2 - Deposit money to an Account:
```bash
curl -X 'PUT' \
  'http://localhost:8080/account/1?amountToDeposit=50' \
  -H 'accept: */*'
```

1.3 - Transfer money between two Accounts
```bash
curl -X 'PUT' \
  'http://localhost:8080/account/transfer?amountToTransfer=3.30&fromId=1&toId=2' \
  -H 'accept: */*'
```

1.4 - Get balance for an Account
```bash
curl -X 'GET' \
  'http://localhost:8080/account/1' \
  -H 'accept: */*'
```

2 - Get exchange rate between DKK and USD
```bash
curl -X 'GET' \
  'http://localhost:8080/exchange/DKK/USD' \
  -H 'accept: */*'
```

3 - Calculate exchange rate between DKK and USD
```bash
curl -X 'POST' \
  'http://localhost:8080/exchange/DKK/USD?amount=100' \
  -H 'accept: */*' \
  -d ''
```