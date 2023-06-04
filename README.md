# Interview Task


### WARNING: Before starting an application you need to insert a GitHub access token to application.properties 
How to retrieve GitHub access token:
https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens

## General info
This project is a simple API that contains only one GET method that allows the consumer to retrieve all repositories from the GitHub account.
Endpoint URL that allows performing GET method:
* http://localhost:8080/api/v1/github/repositories/{userName}
* Application also should contain one Header Content-Type: application/json

If everything goes well, then the repository name, owner login, and all branches will be retrieved with status code 200.
In case invalid username API generates a response with the status 404 and proper message.
Conversely, if the user types an invalid header, API generates a response with a 406 status code and a proper message.