# circuit-breaker
Circuit breaker is kept in three states - OPEN,CLOSED,PARTIALLY_OPEN.

Scenario's 
1. Transition from open to partially open to close can be observed
2. Transition from close to partially open to open can be observed

Clone the project and run Capllary Application.main
Major API'S-
1. http://localhost:8080/status(GET) - to get the status of service(OPEN,CLOSED,PARTIALLY_OPEN).
2. http://localhost:8080/items(GET) - to fetch all items.This will give all the items present.It will increase the count of successful request
3. http://localhost:8080/items(POST) - To create a new Item. This API will fail deliberately to increase count of fail request

payload - {
    "id":"1",
    "name":"item 1",
    "quantity":"10",
    "price":"1000"
}



Configs for requests can be changed from applicationConstants file- 
Current Config - 
1. Max Success request is 7. This denotes number of request after service will definitely be available
2. Max Failed request is 5. This denotes number of request after service will definitely be unavailable
3. Max partial Failed request is 3. This denotes number of request after service is partially 

This Project Works for all test Case Scenario which can be generated.
Test class is not doing justice please clone and try all the scenario.I assure you it will be Fun
Example to see things in action:
To see transition from open to partially open to close -
1. Call http://localhost:8080/items(POST) 4 times and service will go partially available and hitting 4 more times will make service status unavailable.

