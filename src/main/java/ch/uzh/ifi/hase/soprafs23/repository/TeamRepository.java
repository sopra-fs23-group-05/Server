package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("teamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findById(int teamId);
}