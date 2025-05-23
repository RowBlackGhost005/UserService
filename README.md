# Secure Restful API
Small REST API developed in Spring Boot for managing Users with different roles using Spring Security and JWT.

This project is part of CSA and was developed for testing the implementation of JWT and some other security measures.

# How to execute
### Requirements:
* Java 21+
* Maven
* MySQL


### Setps to Execute
1. Clone the repo.
2. Sync the POM dependencies using Maven.
3. Create a database in MySQL called `UsersSecureCSA` (Or any name you want).
4. Configure the Database and Security of the API in the .properties file under `/src/main/resources/application.properties`  locate the following entries:
 ```
		...
		spring.datasource.url=jdbc:mysql://localhost:3306/UsersSecureCSA
		spring.datasource.username=#YourDBUsername
		spring.datasource.password=#YourUserDBPassword
		...
		jwt.secret=#Your-Secret-JWT-Key
		...
		auth.admin.username=#AdminUsername
		auth.admin.password=#AdminPassword
```
* 4.1. Change your datasource.url ONLY if you created a database with another name, to do this simply replace `UsersSecureCSA` with the name of your database.
* 4.2. Replace `#YourDBUsername` with the username of your database user.
* 4.3. Replace `#YourUserDBPassword` with the password of your database user.
* 4.4. Replace `#Your-Secret-JWT-Key` with the key you want to use to sign your JWT tokens. **NOTE: Your key must be at least 16 characters long due to the signing algorithm**
* 4.5 Replace `#AdminUsername` with the username to use to create the Admin account.
* 4.6 Replace `#AdminPassword` with the password to use to create the Admin account.
6. Execute the main file `SecureRestfulApiApplication.java` located under /src/main/java/
7. Wait till the embedded tomcat server and first startup configurations take place.

Once this process is finished you should be able to access the app in `http://localhost:8080`

# Documentation
By this point your API should be up and running, below you will see documentation about the API and some examples as to how to call the API.

You can also access the Swagger endpoint by going to `http://localhost:8080/swagger-ui/index.html` while the API is running.

### Endpoints
This API has two main endpoints: Users and Auth.
/Auth - Manages the registration and login of clients.
/Users - Manages the operations availables for users and manage their data.

The table below shows all active endpoints and how to interact with them:

| Method | Endpoint | Description | Body | Response |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| POST | /api/auth/register | Register the user in the API | Body: `{"username":"Jhon" , "password":"jhonpass"}` | 200 - User Registered correctly / 409 - Username already taken |
| POST | /api/auth/login | Authenticates the user | Body: `{"username":"Jhon" , "password":"jhonpass"}`  | 200 - With the JWT TOKEN / 401 - Bad Credentials |
| GET | /api/users/profile | Returns the current user profile | `--header 'Authorization: Bearer JWTOKEN` | `{"id": 1 , "username":"Jhon"}` |
| GET | /api/users/profile/{id} | Returns a user profile by ID (Only for ADMIN users) | `--header 'Authorization: Bearer JWTOKEN` | `{"id": 3 , "username":"Eli"}` |
| GET | /api/users | Returns a list of all registered users (Only for ADMIN users) | `--header 'Authorization: Bearer JWTOKEN` | `{[{"id": 1 , "username":"Jhon"} , {"id": 3 , "username":"Eli"}]}` |
| DELETE | /api/users/{id} | Deletes an User whose ID matches (Only for ADMIN users) |  `--header 'Authorization: Bearer JWTOKEN` | 200 - If accepted |

# JWT
This API uses JWT for authentication and relies on the clients sending it in every subsequent request.

The Tokens are signed using a secret key that sits in the `application.properties` using SHA256.

Every token holds a signed claim `subject` that holds the clients username and an usigned claim `roles` that holds the roles of a client for authentication and has an expiration of ONE day.

Every time a user sends its request with their JWT Token the API validates it by checking if its expired then by checking if the subject username exists in the database and then retrieves its data for managing its operation.

Note: Only /api/auth has unauthorize access, any other endpoint requieres authorization via Barer JWT.

# Rate Limiter
This API implements a *limited* rate limiter, this limiter only focuses on the /auth endpoints ensuring no brute-force attack can be done.

This is done using the `RateLimterService.class` that holds clients IP against a request counter that should not exceed 3 requests every 60 seconds.

This works by using a `RateLimitInterceptor.class` that serves as a filter between the endpoint and the business logic and it determines whether a request can pass or not.

# Roles
Due to this API rely on Roles they are created automatically in the first launch of the API and they are taken from a Pre-defined .SQL file located under  `/src/main/resources/schema.sql`. This file creates the Roles table and populates it with two basic roles 'USER' and 'ADMIN'.

**NOTE: If you want to add more roles, its name should be on UPPERCASE otherwise it will not work**

Take in mind that a new role will also need some changes in the controllers and config packages of the app and that doc will be added later.

# Admin Credentials
Due to the current state of the API there is no endpoint for promoting Users to admins but the pre-created admin should be all you need for the basic testing of this app.

In later versions of this API there will be endpoints for promoting Users and more functionallity for Admins.

# Credits
Developed by: Luis Marin
