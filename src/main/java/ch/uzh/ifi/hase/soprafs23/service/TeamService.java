package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.custom.Settings;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeamService {

  private final Logger log = LoggerFactory.getLogger(TeamService.class);

  private final TeamRepository teamRepository;
  @Autowired
  public TeamService(@Qualifier("teamRepository") TeamRepository teamRepository) {
      this.teamRepository = teamRepository;
  }


  public Team getTeam(int teamId){return this.teamRepository.findById(teamId);}
    //updates the team at the end of a round with the points they made and switches the roles
    public Team updateTeam(int points,Team team){
      team.setPoints(team.getPoints()+points);
      int teamSize = team.getPlayers().size();
      if (team.getIdxClueGiver()<teamSize){
          team.setIdxClueGiver(team.getIdxClueGiver()+1);
      }else{
          team.setIdxClueGiver(0);
      }
      if (team.getaRole()== Role.BUZZINGTEAM){
          team.setaRole(Role.GUESSINGTEAM);
      }else{
          team.setaRole(Role.BUZZINGTEAM);
      }
      return team;
    }
    public Team createTeam() {
        Team newTeam = new Team();
        newTeam.setPoints(0);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newTeam = teamRepository.save(newTeam);
        teamRepository.flush();

        log.debug("Created Information for Lobby: {}", newTeam);
        return newTeam;
    }

}
