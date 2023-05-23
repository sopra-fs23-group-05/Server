# SoPra RESTful Service Template FS23

## Getting started with Spring Boot
-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: https://spring.io/guides/tutorials/rest/

## Setup this Template with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs23` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

## Debugging
If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
Have a look here: https://www.baeldung.com/spring-boot-testing

# Taboo
## Introduction
This project is an online version of the game Taboo. The objective of the game is for a designated "Clue-Giver" to describe a word without using five specified "taboo words," while his team members attempt to correctly guess the word. The game consists of multiple rounds, which users can determine the number of. Each round consists of two turns. In one turn, a team acts as the guessing team, while in the other turn, they become the Buzzer Team. The Buzzer Team's responsibility is to watch for any accidental use of the taboo words by the Clue-Giver. At the end of all the rounds, the team with the most correctly guessed words is declared the winner. 

The project aims to provide players with the flexibility to enjoy the game both remotely or in person. In the remote mode, players can utilize the chat and buzzer functionality. If the players want to play in-person, they can utilize this application for displaying the cards and keeping score.  

Joining the game is convenient, with options to join remotely through an invite link or access code, or to join in-person through a QR code. Additionally, user-friendly features, such as a dictionary API for word lookup and a share button to showcase achievements at the end of a game, enhance the overall experience of the application. 

Let’s play Taboo! 

## Technologies
We used React with HTML/CSS and JS for the frontend of the application. Further we used: 
- MUI for UI elements 
- QR-Code API to invite people(link) 
- Dictionary API to get definitions of a word(link) 
- Twitter API to share the result(link) 
- Taboo API to get the card data (link) 
- Axios API 

For the backend we used Spring Boot with Java and Gradle. Also, we used: 
- Hibernate as database 
- JPA as Database Interface 
- GitHub Action 
- SonarQube 
- HEROKU for deployment 
- Spring WebSocket 

## High-level components
In the backend we have several main components to handle the game.
- The [GameService](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java) which handles all the requests to the [Game](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java) entity and is also responsible to convert the [Lobby](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Lobby.java) and [Users](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/User.java) to [Game](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java) and [Players](https://github.com/sopra-fs23-group-05/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/custom/Player.java).

## Launch & Deployment
import the Server: 
- Download the code here. (https://github.com/sopra-fs23-group-05/Server) 
- In your IDE, locate the option to import a project or open an existing project (e.g. in IntelliJ, go to “File” > “New” > “Project from Existing Sources”). 
- Select the downloaded file of the server code. 
- Choose the option to import the project as a Gradle project. 
- To build the project either right-click on the “build.gradle” file and select “Run Build” or run “./gradlew build” in your terminal. 
- To run the project locally, use “./gradlew bootRun”. You can verify that the server is running by visiting localhost:8080 in your browser. 
- If you want to run the tests, use “.gradlew test”. 

## Illustrations

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
Check License.md for further information 
