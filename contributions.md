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

# Meeting 20.04.2023 - Week 09
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
- [#79](https://github.com/sopra-fs23-group-05/Server/issues/79) Implemented a team POST request (changed it that teams are automaticly created with the game)
- [#54](https://github.com/sopra-fs23-group-05/Server/issues/54) PUT request for lobby to update the new individual settings (changed it that settings are automaticly changed with the game)
- [#24](https://github.com/sopra-fs23-group-05/Client/issues/24) Created a PreGame Screen that shows before every Turn
- [#47](https://github.com/sopra-fs23-group-05/Server/issues/47) Created Game and Players with the roles
- [#23](https://github.com/sopra-fs23-group-05/Client/issues/23) Get Accescode and Settings for the Game

## Felix
- [#48](https://github.com/sopra-fs23-group-05/Server/issues/48) Implement the whole backend for handling the assignment of new roles before a turn.
- [#50](https://github.com/sopra-fs23-group-05/Server/issues/50) Implement the whole backend for storing the points to the database after a turn.
- [#60](https://github.com/sopra-fs23-group-05/Client/issues/60) Create the chat component including the input field and the send button.
- Most of my time I spent setting up the websocket. It works locally. But at the moment it does not work online. The according tasks are in progress, but not yet completed.
