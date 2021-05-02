package io.ganglas.ipl.Repository;

import org.springframework.data.repository.CrudRepository;

import io.ganglas.ipl.Model.Team;

public interface TeamRepository extends CrudRepository<Team,Long>{
    Team findByTeamName(String teamName);
}
