package io.webapp.ipldashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.webapp.ipldashboard.model.Team;
import io.webapp.ipldashboard.repository.MatchRepository;
import io.webapp.ipldashboard.repository.TeamRepository;

@RestController
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @RequestMapping(value = "/team/{teamName}")
    public Team getTeam(@PathVariable("teamName") String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatch(matchRepository.findLatestMatchesByTeam((String) teamName, 4));
        return team;

    }
}
