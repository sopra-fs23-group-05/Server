package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.custom.Card;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "teamId", target = "teamId")
    @Mapping(source = "aRole", target = "aRole")
    @Mapping(source = "points", target = "points")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "idxClueGiver", target = "idxClueGiver")
    TeamGetDTO convertEntityToTeamGetDTO(Team team);

    @Mapping(source = "rounds", target = "rounds")
    @Mapping(source = "topic", target = "topic")
    @Mapping(source = "roundTime", target = "roundTime")
    Settings convertSettingsPutDTOtoEntity(SettingsPutDTO settingsPutDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "leader", target = "leader")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "leader", target = "leader")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "accessCode", target = "accessCode")
    @Mapping(source = "team1", target = "team1")
    @Mapping(source = "team2", target = "team2")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "accessCode", target = "accessCode")
    @Mapping(source = "settings", target = "settings")
    @Mapping(source = "roundsPlayed", target = "roundsPlayed")
    @Mapping(source = "turn", target = "turn")
    @Mapping(source = "team1", target = "team1")
    @Mapping(source = "team2", target = "team2")
    @Mapping(source = "leader", target = "leader")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "word", target = "word")
    @Mapping(source = "taboo1", target = "taboo1")
    @Mapping(source = "taboo2", target = "taboo2")
    @Mapping(source = "taboo3", target = "taboo3")
    @Mapping(source = "taboo4", target = "taboo4")
    @Mapping(source = "taboo5", target = "taboo5")
    Card convertCardDTOtoEntity(CardDTO cardPostDTO);

}
