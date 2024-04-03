# Players Applications

Application allows to get information about players from Balldontlie’s API.
You can get csv file with information about all players through REST endpoint or receive just receive updated information via WebSocket.
Application uses Ehcache with disk storage and keeps cache for playerId for 15 minutes.
Balldontlie API needs Api-Key for access, so I generated and used my own Api-Key.
You can change settings in [application.yml](src/main/resources/application.yml)

## Endpoint


| Endpoint                                 | README                                                                                                                               |
|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| http://localhost:8081/player             | Returns a CSV file with the all players information (id, first name, last name) with any additional information derived from the API |
| http://localhost:8081/notifications.html | Opens WebSocket with players details update notifications                                                                            |

#### Building

You need maven on your computer and access to its public repository

```sh
mvn clean package
```

Run application

```sh
java -jar players-1.0-SNAPSHOT.jar
```