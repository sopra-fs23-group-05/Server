# Meeting 06.04.2023 - Week 07
## Daniel
- [#34](https://github.com/sopra-fs23-group-05/Client/issues/34) Create homepage for users.
- [#35](https://github.com/sopra-fs23-group-05/Client/issues/35) Create rules page for users.
- [#36](https://github.com/sopra-fs23-group-05/Client/issues/36) Add rules to rules page.

## Melea
- [#38](https://github.com/sopra-fs23-group-05/Client/issues/38) Create login page for lobby leaders.
- [#44](https://github.com/sopra-fs23-group-05/Client/issues/44) Create login page for users.
- [#40](https://github.com/sopra-fs23-group-05/Client/issues/40) Create lobby page with required fields (place for team members, buttons).
- [#41](https://github.com/sopra-fs23-group-05/Client/issues/41) Implement functionality, that buttons "start game" & "settings" are only visible for the lobby leader
- [#85](https://github.com/sopra-fs23-group-05/Server/issues/85) added necessary field isLeader to User in client; felix edited user entity in server (issue 85).
## Felix
- [#97](https://github.com/sopra-fs23-group-05/Server/issues/97) Create and store a new lobby in the database. Created classes: Lobby, Settings, Topic, LobbyGetDTO, LobbyRepository. Implemented six new endpoints in UserController.java and according methods in UserService.
- [#87](https://github.com/sopra-fs23-group-05/Server/issues/87) Store a new user in the database. 12 files changed.
- [#86](https://github.com/sopra-fs23-group-05/Server/issues/86) Implement code to catch non-unique/non-empty usernames.

## Tom
- [#75](https://github.com/sopra-fs23-group-05/Server/issues/75) Wrote a get request for Team. Implementet Team entity, a Player class, TeamGetDto, a Role enum and changed all the depending classes.
- [#76](https://github.com/sopra-fs23-group-05/Server/issues/76) Implemented the put request for a team to update it especially at the end of every round.

# Meeting 20.04.2023 - Week 08
## Daniel
- [#43](https://github.com/sopra-fs23-group-05/Client/issues/43) Create the invite page with invite link and QR code.
- [#96](https://github.com/sopra-fs23-group-05/Server/issues/96) Automatically insert the access code into field after using invite link or QR Code.
- [#48](https://github.com/sopra-fs23-group-05/Client/issues/48) Create settings page where the lobby leader can modify the settings.
- [#49](https://github.com/sopra-fs23-group-05/Client/issues/49) Add functionality to edit number of rounds, timer per round and the topic of words.
- [#10](https://github.com/sopra-fs23-group-05/Client/issues/10) Create the endscreen with the final score and winner displayed.
- [#11](https://github.com/sopra-fs23-group-05/Client/issues/11) Add a back button which will allow players to go back to the home page.
- [#12](https://github.com/sopra-fs23-group-05/Client/issues/11) Add a share button which will allow players to share the final score on Twitter.


## Tom
- [#67](https://github.com/sopra-fs23-group-05/Server/issues/67) Created the Deck class 
- [#66](https://github.com/sopra-fs23-group-05/Server/issues/66) Created the Card class
- [#73](https://github.com/sopra-fs23-group-05/Server/issues/73) Created the Turn class
- [#79](https://github.com/sopra-fs23-group-05/Server/issues/79) Implemented a team POST request
- [#24](https://github.com/sopra-fs23-group-05/Client/issues/24) Created a PreGame Screen that shows before every Turn
- [#47](https://github.com/sopra-fs23-group-05/Server/issues/47) Created Game and Players with the roles
- [#23](https://github.com/sopra-fs23-group-05/Client/issues/23) Get Accescode and Settings for the Game

## Felix
- [#48](https://github.com/sopra-fs23-group-05/Server/issues/48) Implement the whole backend for handling the assignment of new roles before a turn.
- [#50](https://github.com/sopra-fs23-group-05/Server/issues/50) Implement the whole backend for storing the points to the database after a turn.
- [#60](https://github.com/sopra-fs23-group-05/Client/issues/60) Create the chat component including the input field and the send button.
- Most of my time I spent setting up the websocket. It works locally. But at the moment it does not work online. The according tasks are in progress, but not yet completed.

## Melea
- [#37](https://github.com/sopra-fs23-group-05/Client/issues/37) Implement redirect to Login Page for lobby leaders
- [#45](https://github.com/sopra-fs23-group-05/Client/issues/45) If Login fails, display error and reload Login page
- [#56](https://github.com/sopra-fs23-group-05/Client/issues/56) Create Game page
- [#57](https://github.com/sopra-fs23-group-05/Client/issues/57) Create Card component with Buttons for Skip and Word
- [#58](https://github.com/sopra-fs23-group-05/Client/issues/58) Create Card component without Buttons
- [#59](https://github.com/sopra-fs23-group-05/Client/issues/59) Create Timer/Score component
- [#61](https://github.com/sopra-fs23-group-05/Client/issues/61) Create the Buzzer component


# Meeting 27.04.2023 - Week 09
## Daniel
- [#1](https://github.com/sopra-fs23-group-05/Client/issues/1) On the game page of the clue-giver make the word button clickable.
- [#2](https://github.com/sopra-fs23-group-05/Client/issues/2) Edit the game page of the clue giver so that the description is displayed when he clicks on the word button.
- [#7](https://github.com/sopra-fs23-group-05/Client/issues/7) Implement a function that will display the guess entered on the display.
- [#97](https://github.com/sopra-fs23-group-05/Client/issues/97) Dynamically look up word of current card in dictionary.
- [#103](https://github.com/sopra-fs23-group-05/Client/issues/103) Create a timer function which starts counting down when a new turn starts.
- [#6](https://github.com/sopra-fs23-group-05/Server/issues/6) As a lobby leader I want to change the settings to change the number of rounds, the time per round and the topic of the words.
- [#91](https://github.com/sopra-fs23-group-05/Server/issues/91) Implement a mechanism for generating unique access codes for each lobby.

## Tom
- [#54](https://github.com/sopra-fs23-group-05/Server/issues/54) Write PUT request for lobbies. Update the lobby with the new settings
- [#9](https://github.com/sopra-fs23-group-05/Client/issues/9) Implement a function which displays the timer, the player’s new role for the next turn and the scoreboard of both teams.
- [#140](https://github.com/sopra-fs23-group-05/Server/issues/140) Add turn points to guesser teams total points after a round.

## Felix
- [#115](https://github.com/sopra-fs23-group-05/Server/issues/115), [#118](https://github.com/sopra-fs23-group-05/Server/issues/118), [#45](https://github.com/sopra-fs23-group-05/Server/issues/45), [#46](https://github.com/sopra-fs23-group-05/Server/issues/46), [#31](https://github.com/sopra-fs23-group-05/Client/issues/31), [#63](https://github.com/sopra-fs23-group-05/Client/issues/63), [#64](https://github.com/sopra-fs23-group-05/Client/issues/64), [#65](https://github.com/sopra-fs23-group-05/Client/issues/65), [#66](https://github.com/sopra-fs23-group-05/Client/issues/66) Implement the chat for the game view using a websocket

- [#31](https://github.com/sopra-fs23-group-05/Server/issues/31), [#70](https://github.com/sopra-fs23-group-05/Server/issues/70), [#120](https://github.com/sopra-fs23-group-05/Server/issues/120), [#29](https://github.com/sopra-fs23-group-05/Client/issues/29) Implement a websocket for sending and receiving cards between the client and the server.

- [#3](https://github.com/sopra-fs23-group-05/Client/issues/3), [#4](https://github.com/sopra-fs23-group-05/Client/issues/4), [#28](https://github.com/sopra-fs23-group-05/Server/issues/28), [#29](https://github.com/sopra-fs23-group-05/Server/issues/29), [#32](https://github.com/sopra-fs23-group-05/Server/issues/32) Enable skipping a card and buzzing.

- [#120](https://github.com/sopra-fs23-group-05/Server/issues/120) Connect the chat websocket with the card websocket. The use case is, when a guess entered via the chat, the client needs a new card.

## Melea
 - [#21](https://github.com/sopra-fs23-group-05/Client/issues/21) Update the component displaying the players name in the team he chose.
 - [#13](https://github.com/sopra-fs23-group-05/Client/issues/13) Create one box for each team, with a text field indicating each team's name.
 - [#14](https://github.com/sopra-fs23-group-05/Client/issues/14) Display the players that already joined a team.
 - [#35](https://github.com/sopra-fs23-group-05/Server/issues/35) Disable the start game button based on the condition that both teams need at least two players.
 - [#15](https://github.com/sopra-fs23-group-05/Client/issues/15) Create a join button that sends a request to the backend for a user to join a team.
 - [#68](https://github.com/sopra-fs23-group-05/Server/issues/68) Connect the Taboo database to our app. 
 - [#62](https://github.com/sopra-fs23-group-05/Client/issues/62) Display the components depending on the players role.
 - [#150](https://github.com/sopra-fs23-group-05/Server/issues/150) Make sure a user can only join one team.

# Meeting 04.05.2023 - Week 10
## Felix
- [#163](https://github.com/sopra-fs23-group-05/Server/issues/163) Use WebSocket for joining a team (back-end).
- [#112](https://github.com/sopra-fs23-group-05/Client/issues/112) Use WebSocket for joining a team (front-end).
- [#142](https://github.com/sopra-fs23-group-05/Client/issues/142) Switch URLs dynamically between dev and prod.
- [#143](https://github.com/sopra-fs23-group-05/Client/issues/143) Adjust the message type dynamically between clue-giver and guesser.

## Tom
- [#185](https://github.com/sopra-fs23-group-05/Server/issues/185) Write a request to delete the game,lobby, players and teams(end of game).
- [#195](https://github.com/sopra-fs23-group-05/Server/issues/195) Write a request to delete the lobby and the users (lobby leader quitting the lobby).
- [#129](https://github.com/sopra-fs23-group-05/Client/issues/129) Write Guards for the different pages that unautorized User can't access them. (not commited to the main branch, because it's easier to test the client without).

## Melea
- [#188](https://github.com/sopra-fs23-group-05/Server/issues/188) Display the correct Score of the guessing team in the Game page.
- [#160](https://github.com/sopra-fs23-group-05/Server/issues/160) Make sure that joining a team is only possible if the teams do not become unfair.
- [#199](https://github.com/sopra-fs23-group-05/Server/issues/199) Implement the functionality, that a player can switch between the two teams in the lobby if he wants to.

## Daniel
- [#174](https://github.com/sopra-fs23-group-05/Server/issues/174) Create a function to determine the MVP player of the game.
- [#118](https://github.com/sopra-fs23-group-05/Client/issues/118) Implement a leave button for the players.
- [#130](https://github.com/sopra-fs23-group-05/Client/issues/130) Change the topic buttons on the Settings page according to the Taboo Data.
- [#135](https://github.com/sopra-fs23-group-05/Client/issues/135) Create a component to display the MVP player and the number of rounds played.

# Meeting 11.05.2023 - Week 11
## Daniel
- [#184](https://github.com/sopra-fs23-group-05/Server/issues/184) Adjust the Topic enum according to the topics in the Taboo Data.
- [#127](https://github.com/sopra-fs23-group-05/Client/issues/127) Implement a confirmation window when a non-leading player wants to leave the game.
- [#128](https://github.com/sopra-fs23-group-05/Client/issues/128) Adjust the size of the "word button" on the game page to the length of the card word.
- [#155](https://github.com/sopra-fs23-group-05/Client/issues/155) Create a component to display the points of each player at the end of the game.
- [#156](https://github.com/sopra-fs23-group-05/Client/issues/156) Change the link in the tweet to the link of the homepage.

## Felix
- [#206](https://github.com/sopra-fs23-group-05/Server/issues/206) Record the collected points of each individual player.
- [#208](https://github.com/sopra-fs23-group-05/Server/issues/208) WS for turning pages.
- [#119](https://github.com/sopra-fs23-group-05/Client/issues/119) WS for turning pages - Lobby to PreGame
- [#121](https://github.com/sopra-fs23-group-05/Client/issues/121) WS for turning pages - PreGame to Game
- [#123](https://github.com/sopra-fs23-group-05/Client/issues/123) WS for turning pages - Game to PreGame
- [#125](https://github.com/sopra-fs23-group-05/Client/issues/125) WS for turning pages - Game to EndScreen

## Tom
- [#131](https://github.com/sopra-fs23-group-05/Client/issues/131) Leader closes Endscreen delete Game and Players.
- [#153](https://github.com/sopra-fs23-group-05/Client/issues/153) Leader closes Lobby delete Users and Lobby.
- [#167](https://github.com/sopra-fs23-group-05/Server/issues/167) Endpoint and method to finish the game.

## Melea
- [#122](https://github.com/sopra-fs23-group-05/Client/issues/122) Disable the "Start Game" button on the lobby page if the teams do not contain at least 2 players
- [#159](https://github.com/sopra-fs23-group-05/Client/issues/159) Use drop-down menus instead of TextFields for changing the settings
- [#117](https://github.com/sopra-fs23-group-05/Client/issues/117) Show a message that a new card is drawn -> does not work yet 

# Meeting 18.05.2023 - Week 12
## Felix
- [#215](https://github.com/sopra-fs23-group-05/Server/issues/215) Separate WS sessions for each game.
- [#163](https://github.com/sopra-fs23-group-05/Server/issues/163) Fix joining teams.
- [#179](https://github.com/sopra-fs23-group-05/Client/issues/179) Enable players to switch between the teams.
- [#235](https://github.com/sopra-fs23-group-05/Server/issues/235) Display error messages in production.
- [#237](https://github.com/sopra-fs23-group-05/Server/issues/237) Forbid usernames that contain nothing but spaces. (Feedback beta testing)

## Melea
- [#158](https://github.com/sopra-fs23-group-05/Client/issues/158) Enable the ENTER key for user input.
- [#174](https://github.com/sopra-fs23-group-05/Client/issues/174) Remove scroll bar in timer and card.
- [#175](https://github.com/sopra-fs23-group-05/Client/issues/175) Add leave button on top of the Game page for non-leader players.
- [#173](https://github.com/sopra-fs23-group-05/Client/issues/173) Adjust the layout of the chat component in the Game page.
- [#177](https://github.com/sopra-fs23-group-05/Client/issues/177) Make formatting consistent across all pages.
- [#188](https://github.com/orgs/sopra-fs23-group-05/projects/2/views/1?filterQuery=melea&pane=issue&itemId=28415603) remove word "QUICK" in the rules page.

## Daniel
- [#176](https://github.com/sopra-fs23-group-05/Server/issues/176) Make a endpoint which removes a player from a team.
- [#177](https://github.com/sopra-fs23-group-05/Server/issues/177) Implement a function to check if there are enough players to continue the game.
- [#178](https://github.com/sopra-fs23-group-05/Server/issues/178) Implement a function which ends the game if there are not enough players left.
- [#184](https://github.com/sopra-fs23-group-05/Client/issues/184) Redirect the remaining players to the endscreen if there are too few players in the game.
- [#195](https://github.com/sopra-fs23-group-05/Client/issues/195) Implement button sound such that a sound is played when a user clicks a button.
- [#198](https://github.com/sopra-fs23-group-05/Client/issues/198) Implement a sound effect when a player joins a team.
- [#199](https://github.com/sopra-fs23-group-05/Client/issues/199) Implement a sound effect so that a sound is played when the guessing player makes a guess.
- [#200](https://github.com/sopra-fs23-group-05/Client/issues/200) Implement a sound effect so that a sound is played when the clue-giving player provides a clue.
- [#201](https://github.com/sopra-fs23-group-05/Client/issues/201) Implement a sound effect so that a sound is played when the buzzer button is clicked.
- [#203](https://github.com/sopra-fs23-group-05/Client/issues/203) Implement a winner sound that will be played at the end screen. 

## Tom
- [#189](https://github.com/sopra-fs23-group-05/Client/issues/189) Adjust settings that sliders are on current settings.
- [#49](https://github.com/sopra-fs23-group-05/Server/issues/49) Changed the timer that it works with the websocket and adjusted the frontend.

# Meeting 25.05.2023 - Week 13
## Tom
- [#225](https://github.com/sopra-fs23-group-05/Server/issues/225) Make the buzzer only avaiable once per card and player.
- [#288](https://github.com/sopra-fs23-group-05/Server/issues/282) Delete Users who are not in a Team at roundstart.
- [#283](https://github.com/sopra-fs23-group-05/Server/issues/283) Change TeamWebsocket for Users who leave the Lobby.

## Daniel
- [#271](https://github.com/sopra-fs23-group-05/Server/issues/271) Implement a function that determines how many users must join to start the game.
- [#196](https://github.com/sopra-fs23-group-05/Client/issues/196) Implement the background music for the game.
- [#197](https://github.com/sopra-fs23-group-05/Client/issues/197) Add a music button to the game that allows the user to turn background music on and off.
- [#230](https://github.com/sopra-fs23-group-05/Client/issues/230) Implementation of a notification sound that is played when a new card is drawn.
- [#238](https://github.com/sopra-fs23-group-05/Client/issues/238) Display the remaining users to start the game.
- [#258](https://github.com/sopra-fs23-group-05/Client/issues/258) When clicking the Start Game button, change the button to Loading.

## Felix
- [#168](https://github.com/sopra-fs23-group-05/Server/issues/168) Send information to clients when new card is drawn.
- [#247](https://github.com/sopra-fs23-group-05/Server/issues/247) Fix card ws.
- [#249](https://github.com/sopra-fs23-group-05/Server/issues/249) Fix timer ws.
- [#101](https://github.com/sopra-fs23-group-05/Client/issues/101) Correct the roles of the teams and players in Game.js.

## Melea
- [#113](https://github.com/sopra-fs23-group-05/Client/issues/113) Display a “Finish” Button on Game page of the lobby leader.
- [#114](https://github.com/sopra-fs23-group-05/Client/issues/114) Implement event listener when the “Finish” button is clicked.
- [#115](https://github.com/sopra-fs23-group-05/Client/issues/115) Send a request to the server to indicate that the game has finished.
- [#116](https://github.com/sopra-fs23-group-05/Client/issues/116) Redirect players to endscreen.
- [#117](https://github.com/sopra-fs23-group-05/Client/issues/117) Show a mesage that a new card is drawn.

