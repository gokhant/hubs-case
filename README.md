# HUBS Case Project Notes
- Project uses in-memory H2 DB
- While application is running, a web-console for H2 DB is available at http://localhost:8080/h2-console, DB can be accessed by using `jdbc:h2:mem:wallet-db` as JDBC URL.
- Additionally, actuator is available at http://localhost:8080/actuator w/ info (empty) and health endpoints
- In order to run the project below is sufficient
> mvn spring-boot:run
- Swagger of the API is available at http://localhost:8080/swagger-ui/index.html while the application is running.
- An api at /api/v1/simulate-login is added to get JWT to access rest of the APIs of the system. Rest of the APIs require this token as Bearer in Authorization header.
> curl --location 'http://localhost:8080/api/v1/simulate-login' \
--header 'Content-Type: application/json' \
--data '{
"userName": "Desmond29",
"customerId": -1,
"userRole": "CUSTOMER",
"jwtExpiryInSeconds": 86400
}'

### /api/v1/customers/{customerId}/wallets
- [POST] to create a wallet
- [GET] to list wallets

### /api/v1/customers/{customerId}/wallets/{walletId}
- /deposit [POST] to deposit
- /withdrawal [POST] to make a withdrawal

### /api/v1/customers/{customerId}/wallets/{walletId}/transactions
- [GET] to list transactions on a certain wallet
- [PUT] to change the status of a pending transaction on a certain wallet (**EMPLOYEE** access only)




