package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class TeamController {

  private final TeamService teamService;

  TeamController( TeamService teamService) {
      this.teamService = teamService;

  }

  @GetMapping("/teams/{teamId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public TeamGetDTO getTeam(@PathVariable("teamId")int teamId) {
      Team team = teamService.getTeam(teamId);

      return DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);
  }
  @PutMapping ("/teams/{teamId}/points/{points}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public TeamGetDTO updateTeam(@PathVariable int points,@PathVariable int teamId) {
        Team team = teamService.getTeam(teamId);
        teamService.updateTeam(points, team);

        return DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);
  }
}



