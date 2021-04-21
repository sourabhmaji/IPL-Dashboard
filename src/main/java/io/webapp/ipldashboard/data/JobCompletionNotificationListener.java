package io.webapp.ipldashboard.data;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.webapp.ipldashboard.model.Team;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  private final EntityManager em;

  @Autowired
  public JobCompletionNotificationListener(EntityManager em) {
    this.em = em;
  }

  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

      Map<String, Team> teamMap = new HashMap<>();
      em.createQuery("Select  m.firstInningsTeam ,count(*) from Match m group by m.firstInningsTeam",
          Object[].class).getResultList().stream().map(e -> new Team((String) e[0], (long) e[1]))
          .forEach(teamObj -> teamMap.put(teamObj.getTeamName(), teamObj));
      

      em.createQuery("Select m.secondInningsTeam , count(*) from Match m group by m.secondInningsTeam",
          Object[].class).getResultList().stream().forEach(e -> {
            Team team = teamMap.get((String) e[0]);
            team.setTotalMatches((long) e[1] + team.getTotalMatches());
          });

      em.createQuery("Select count(m.matchWinner) ,m.matchWinner from Match m group by country", Object[].class)
          .getResultList().stream().forEach(e -> {
            Team team = teamMap.get((String) e[1]);
            if (team != null)
              team.setTotalWins((long) e[0]);
          });

      teamMap.values().forEach(team -> em.persist(team));

      teamMap.values().forEach(e -> System.out.println(e));

    }
  }
}