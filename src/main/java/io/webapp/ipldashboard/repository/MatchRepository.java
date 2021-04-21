package io.webapp.ipldashboard.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.webapp.ipldashboard.model.Match;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> getByFirstInningsTeamOrSecondInningsTeamOrderByDateDesc(String firstInningsTeam, String secondInningsTeam ,Pageable pageable );

    default List<Match> findLatestMatchesByTeam(String teamName, int count) {

        return getByFirstInningsTeamOrSecondInningsTeamOrderByDateDesc((String) teamName, (String) teamName,
                PageRequest.of(0, count));
        

    }
}
