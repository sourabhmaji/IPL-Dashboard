package io.webapp.ipldashboard.repository;

import org.springframework.data.repository.CrudRepository;

import io.webapp.ipldashboard.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {

    Team findByTeamName(String teamName);

}
