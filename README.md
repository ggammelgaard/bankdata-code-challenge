# Bankdata Code Challenge

Author: Gustav A. G.

Implemented in Java with the Quarkus framework, Maven dependency manager, and H2 in-memory database. The web-application is written in JavaScript using the React library.

The application starts with two Accounts in the database, having account number (id) 1 and 2. See `.\src\main\resources\import.sql` for details.

## Examples of application
See the folder `.\images\`

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
The frontend is accessible at http://localhost:3000/.

The backend's OpenAPI documentation is available at http://localhost:8080/swagger.

### Curl commands:
1.1 - Create a new Account (response is the new account number): 
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