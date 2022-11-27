##  RESTful API for money transfers between accounts
# Technologies
 - Java 17
 - Spring boot
 - Spring data
 - liquid-base
 - H2 in memory database

# How to run
Clone project, run MainApplication from IDE or 
build and run project by executing following commands 

```bash
./gradlew build
./gradlew bootRun
```

# Application usage
Create an account
````
POST http://localhost:8082/api/v1/accounts/
Content-Type: application/json
{
	"username" : "testusername",
	"email" : "testemail@gop.com",
	"balance" :10,
	"currencyCode" : "usd"
}
````
Response
````
HTTP 201 Created
{
    "accountId": 1,
    "username": "testusername",
    "email": "testemail@gop.com",
    "balance": 10,
    "currencyCode": "usd"
}
````
Get account by id
````
GET http://localhost:8082/api/v1/account/{accountId}/
````
Response
````
HTTP 200 OK
{
    "accountId": 1,
    "username": "testusername",
    "email": "testemail@gop.com",
    "balance": 10.00,
    "currencyCode": "usd"
}
````
Get all accounts
````
GET http://localhost:8082/api/v1/accounts/
````
Response
````
HTTP 200 OK
<response body>
````

Update account data
````
PUT http://localhost:8082/api/v1/accounts/
Content-Type: application/json

{
    "accountId": 1,
    "username": "testusername",
    "email": "testemail@gop.com",
    "balance": 10,
    "currencyCode": "usd"
}
````
Response
````
HTTP 204 No Content
````

Delete account 
````
DELETE http://localhost:8082/api/v1/accounts/{accountId}/
````
Response
````
HTTP 204 NO CONTENT
````
Create new transaction - transfer money from one account to another
````
POST  http://localhost:8082/api/v1/accounts/{accountId}/transactions/
Content-Type: application/json

{
    "toAccountId": 2,
    "amount": "2",
    "currencyCode": "usd",
    "message" : "transfer title 1"
}
````
Response
````
HTTP 201 Created
````
Get all transactions for given account
````
GET  http://localhost:8082/api/v1/accounts/{accountId}/transactions/
````
Response
````
HTTP 200 OK
<body response>
````



