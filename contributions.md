Before each meeting, each team member is required to manually append the two development tasks they completed during the previous week. Each entry should consist of the (week-) date, your name, links to the GitHub Issues you worked on, and optionally a short description of your work.

# Meeting 06.04.2023 - Week 07
## Daniel
- [#34] (https://github.com/sopra-fs23-group-05/Client/issues/34): Create homepage for users.
- [#35] (https://github.com/sopra-fs23-group-05/Client/issues/35): Create rules page for users.
- [#36] (https://github.com/sopra-fs23-group-05/Client/issues/36): Add rules to rules page.

## Melea
- [#38] (https://github.com/sopra-fs23-group-05/Client/issues/38): Create login page for lobby leaders.
- [#44] (https://github.com/sopra-fs23-group-05/Client/issues/44): Create login page for users.
- [#40] (https://github.com/sopra-fs23-group-05/Client/issues/40): Create lobby page with required fields (place for team members, buttons).
- [#41| (https://github.com/sopra-fs23-group-05/Client/issues/41): Implement functionality, that buttons "start game" & "settings" are only visible for the lobby leader
- [#85] (https://github.com/sopra-fs23-group-05/Server/issues/85): added necessary field isLeader to User in client; felix edited user entity in server (issue 85).
## Felix
- [#97](https://github.com/sopra-fs23-group-05/Server/issues/97): Create and store a new lobby in the database. Created classes: Lobby, Settings, Topic, LobbyGetDTO, LobbyRepository. Implemented six new endpoints in UserController.java and according methods in UserService.
- [#87](https://github.com/sopra-fs23-group-05/Server/issues/87): Store a new user in the database. 12 files changed.
- [#86](https://github.com/sopra-fs23-group-05/Server/issues/86): Implement code to catch non-unique/non-empty usernames.

## Tom
- [#75](https://github.com/sopra-fs23-group-05/Server/issues/75): Wrote a get request for Team. Implementet Team entity, a Player class, TeamGetDto, a Role enum and changed all the depending classes.
- [#76](https://github.com/sopra-fs23-group-05/Server/issues/76): Implemented the put request for a team to update it especially at the end of every round.

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
