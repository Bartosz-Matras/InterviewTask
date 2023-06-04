# Interview Task


### WARNING: Before starting application you need to insert github acces token to application.properties 

## General info
This project is a simple API that contains only one GET method that allows the consumer to retrieve all repositories from the GitHub account.
Endpoint URL that allows performing GET method:
* http://localhost:8080/api/v1/github/repositories/{userName}
* Application also should contain one Header Content-Type: application/json

If everything goes well, then repository name, owner login and all branches will be retrieved with status code 200.
In case invalid username API generates a response with the status 404 and proper message.
Conversely, if the user types an invalid header, API generates a response with a 406 status code and a proper message.