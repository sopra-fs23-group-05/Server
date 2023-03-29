package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long>{
    Lobby findByAccessCode(int accessCode);
}