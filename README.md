# SmartJob 

## API User

Microservice that generate token and create new users

## Diagram of solution
![Diagram of solution](solution-diagram.png")

### Instructions for consume API
> ❗Important: for consume this API, first you need generate JWT Token

- Download and configure jdk 17, maven 3.8.8 in IDE
- Compile microservice with maven command:
```
mvn clean install
mvn spring-boot:run
```
- test endpoints generate token and create user

### Test microservice 

#### Generate token
- Request
```
curl --location -X GET 'http://localhost:8080/generateToken?username=user&password=password'
```
- Response
```
eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZW1vLXdlYmZsdXgtc21hcnRqb2IiLCJzdWIiOiJ1c2VyIiwicGFzc3dvcmQiOiJwYXNzd29yZCIsImV4cCI6MTczMjc0MjAzNX0.bai6jCCDaHoH3BoQvObp8zm4ZIhDr3WFs2HlzSmgC58
```
  
#### Create user

<details>
<summary>Request Success: 201</summary>

```
curl --location -v POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZW1vLXdlYmZsdXgtc21hcnRqb2IiLCJzdWIiOiJ1c2VyIiwicGFzc3dvcmQiOiJwYXNzd29yZCIsImV4cCI6MTczMjc0MjAzNX0.bai6jCCDaHoH3BoQvObp8zm4ZIhDr3WFs2HlzSmgC58' \
--data-raw '{
           "name": "Jose",
           "email": "jose@smartjob.cl",
           "password": "1223abcd",
           "phones": [
            {
                "number": "9999999",
                "citycode": "1",
                "contrycode": "51"
            }
           ]
         }'
```
```
* upload completely sent off: 284 bytes
< HTTP/1.1 201 Created
< Content-Type: application/json
< Content-Length: 404
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Content-Type-Options: nosniff
```
</details>

<details>
<summary>Bad Request: 400 User already exist</summary>

```
curl --location -v POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZW1vLXdlYmZsdXgtc21hcnRqb2IiLCJzdWIiOiJ1c2VyIiwicGFzc3dvcmQiOiJwYXNzd29yZCIsImV4cCI6MTczMjc0MjAzNX0.bai6jCCDaHoH3BoQvObp8zm4ZIhDr3WFs2HlzSmgC58' \
--data-raw '{
           "name": "Jose",
           "email": "jose@smartjob.cl",
           "password": "1223abcd",
           "phones": [
            {
                "number": "9999999",
                "citycode": "1",
                "contrycode": "51"
            }
           ]
         }'
```
```
* upload completely sent off: 283 bytes
< HTTP/1.1 400 Bad Request
< Content-Type: application/json
< Content-Length: 37
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Content-Type-Options: nosniff
< X-Frame-Options: DENY
< X-XSS-Protection: 0
< Referrer-Policy: no-referrer
<
{ [37 bytes data]
100   320  100    37  100   283    273   2089 --:--:-- --:--:-- --:--:--  2352
{"mensaje":"El correo ya registrado"}
```
</details>

<details>
<summary>Bad Request invalid: 400</summary>

```
curl --location -v POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZW1vLXdlYmZsdXgtc21hcnRqb2IiLCJzdWIiOiJ1c2VyIiwicGFzc3dvcmQiOiJwYXNzd29yZCIsImV4cCI6MTczMjc0MjAzNX0.bai6jCCDaHoH3BoQvObp8zm4ZIhDr3WFs2HlzSmgC58' \
--data-raw '{
           "name": "Vladimir",
           "email": "vladimirsmartjob.cl",
           "password": "1223abcd",
           "phones": [
            {
                "number": "9999999",
                "citycode": "1",
                "contrycode": "51"
            }
           ]
         }'
```
```
* upload completely sent off: 290 bytes
< HTTP/1.1 400 Bad Request
< Content-Type: application/json
< Content-Length: 1378
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Content-Type-Options: nosniff
```
</details>

<details>
<summary>Unauthorized: 401</summary>

```
curl --location -v POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
           "name": "Vladimir",
           "email": "vladimirsmartjob.cl",
           "password": "1223abcd",
           "phones": [
            {
                "number": "9999999",
                "citycode": "1",
                "contrycode": "51"
            }
           ]
         }'
```
```
* upload completely sent off: 290 bytes
< HTTP/1.1 401 Unauthorized
< WWW-Authenticate: Basic realm="Realm"
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Content-Type-Options: nosniff
```
</details>

#### Contract Open Apí
[contrato microservicio](/src/main/resources/openapi.yaml)

@Autor: [Roger Huamani Ledesma](https://www.linkedin.com/in/roger-vladimir-huamani-ledesma-58887a11a/?originalSubdomain=pe)
