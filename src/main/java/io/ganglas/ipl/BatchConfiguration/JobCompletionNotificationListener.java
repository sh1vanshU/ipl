package io.ganglas.ipl.BatchConfiguration;

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

import io.ganglas.ipl.Model.Team;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            // This execution prints data from match table.
            // jdbcTemplate.query("SELECT team1, team2, date FROM match",(rs, row) -> "Team1
            // "+rs.getString(1)+ "\n Team2 "+ rs.getString(2)+"\n Date :
            // "+rs.getString(3)).forEach(str -> System.out.println(str));
            Map<String, Team> teamData = new HashMap<>();

            entityManager.createQuery("Select m.team1,count(*) from Match m group by m.team1", Object[].class)
                    .getResultList().stream().map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));
            entityManager.createQuery("Select m.team2,count(*) from Match m group by m.team2", Object[].class)
                    .getResultList().stream()
                    .forEach(e -> {
                        Team team=teamData.get((String)e[0]);
                        team.setTotalMatches(team.getTotalMatches()+ (long)e[1]);
                        teamData.put((String)e[0],team);
                    });
            entityManager.createQuery("Select m.matchWinner,count(*) from Match m group by m.matchWinner", Object[].class)
                    .getResultList().stream()
                    .forEach(e -> {
                        Team team=teamData.get((String)e[0]);
                        if(team!=null){
                            team.setTotalWins((long)e[1]);
                            team.setWinningAccuracy((team.getTotalWins()*100)/team.getTotalMatches());
                        }
                    });

            teamData.values().forEach(team-> {
                entityManager.persist(team);
                // System.out.println(team.toString());
            });
            


        }
    }
}