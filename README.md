# Taboo
## Introduction
This project is an online version of the game Taboo. The objective of the game is for a designated "Clue-Giver" to describe a word without using five specified "taboo words", while his team members attempt to guess the word correctly. The game consists of multiple rounds, which users can determine the number of. Each round consists of two turns. In one turn, a team acts as the guessing team, while in the other turn, they become the Buzzer Team. The Buzzer Team's responsibility is to watch for any accidental use of the taboo words by the Clue-Giver. At the end of all the rounds, the team with the most correctly guessed words is declared the winner.

The project aims to provide players with the flexibility to enjoy the game both remotely or in person. In the remote mode, players can utilize the chat and buzzer functionality. If the players want to play in-person, they can utilize this application for displaying the cards and keeping score.

Joining the game is convenient, with options to join remotely through an invite-link or access code, or to join in-person through a QR code. Additionally, user-friendly features, such as a dictionary API for word lookup and a share button to showcase achievements at the end of a game, enhance the overall experience of the application.

Let’s play Taboo!

## Technologies
- Spring Boot with Java and Gradle
- Hibernate as database 
- JPA as Database Interface 
- GitHub Action 
- SonarQube 
- HEROKU for deployment 
- Spring WebSocket 

## High-level components
In the backend we have several main components to handle the game.
- The [GameService](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java) which handles all the requests to the [Game](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java) entity and is also responsible to convert the [Lobby](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Lobby.java) and [Users](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/User.java) to [Game](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java) and [Players](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/custom/Player.java).
- The [LobbyService](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/LobbyService.java) is responsible for gathering all the [Users](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/User.java), assign them to Teams and let the leader adjust the [Settings](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/custom/Settings.java) for the [Game](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java).
- The [ChatWebSocket](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/websockets/ChatWebSocketHandler.java) handles the realtime chat interaction between all the [Players](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/custom/Player.java) during the [Game](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java).

## Launch & Deployment
import the Server: 
- Download the code here. (https://github.com/sopra-fs23-group-05/Server) 
- In your IDE, locate the option to import a project or open an existing project (e.g. in IntelliJ, go to “File” > “New” > “Project from Existing Sources”). 
- Select the downloaded file of the server code. 
- Choose the option to import the project as a Gradle project. 
- To build the project either right-click on the “build.gradle” file and select “Run Build” or run “./gradlew build” in your terminal. 
- To run the project locally, use “./gradlew bootRun”. You can verify that the server is running by visiting localhost:8080 in your browser. 
- If you want to run the tests, use “.gradlew test”. 

## Roadmap
Possible features that new developers can add: 
- A canvas to also draw the word instead of describing it. 
- Feedback that shows if you are close to the word (if there is a spelling mistake etc.) 
- Make it possible to use more than just one topic for the card deck. 

## Authors
- Felix Merz 
- Melea Köhler 
- Daniel Maksimovic 
- Tom Meier

## License
Check [License.md](https://github.com/sopra-fs23-group-05/Server/blob/main/LICENSE) for further information 
