package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(@Qualifier("teamRepository") TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }


    public Team getTeam(int teamId) {
        return this.teamRepository.findById(teamId);
    }

    public Team createTeam(List<User> users, Role role) {
        List<Player> players = new ArrayList<>();
        for (User temp : users) {
            Player player = convertUserToPlayer(temp.getId());
            players.add(player);
        }

        if (players.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each team must at least contain two players");
        }

        Team newTeam = new Team(players, role);


        // saves the given entity but data is only persisted in the database once// flush() is called
        newTeam = teamRepository.save(newTeam);
        teamRepository.flush();

        log.debug("Created Information for Lobby: {}", newTeam);
        return newTeam;
    }

    public Player convertUserToPlayer(long userId) {
        User user = userRepository.findById(userId);
        return new Player(user.getUsername(), user.isLeader());
    }

    public void changeTurn(int teamId1, int teamId2, int scoredPoints) {
        Team team1 = teamRepository.findById(teamId1);
        Team team2 = teamRepository.findById(teamId2);
        if (team1 == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team1 not found");
        }
        else if (team2 == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team2 not found");
        }

        scoredPoints = Math.max(scoredPoints, 0);   // Team has at least 0 points after guessing
        team1.changeTurn(scoredPoints);
        team2.changeTurn(scoredPoints);

        teamRepository.save(team1);
        teamRepository.save(team2);
        // Do we need to flush here?
        teamRepository.flush();
        log.debug("Updated points for guessing team and switched roles.");
    }

    public boolean isInTeam(int teamId, String userName) {
        Team team = teamRepository.findById(teamId);
        if (userRepository.findByUsername(userName) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        for (int i = 0; i < team.getPlayers().size(); i++) {
            Player player = team.getPlayers().get(i);
            if (Objects.equals(player.getName(), userName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClueGiver(int teamId, String userName) {
        Team team = teamRepository.findById(teamId);
        for (int i = 0; i < team.getPlayers().size(); i++) {
            Player player = team.getPlayers().get(i);
            if (Objects.equals(player.getName(), userName) && i == team.getIdxClueGiver()) {
                return true;
            }
        }
        return false;

    }

    public void increasePlayerScore(int teamId, String userName) {
        Team existingTeam = teamRepository.findById(teamId);
        if (existingTeam == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        existingTeam.increasePlayerScore(userName);
    }

    public Role getTeamRole(int teamId) {
        Team existingTeam = teamRepository.findById(teamId);
        if (existingTeam == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        return existingTeam.getaRole();
    }
}
