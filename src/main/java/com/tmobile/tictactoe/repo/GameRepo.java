package com.tmobile.tictactoe.repo;

import com.tmobile.tictactoe.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {

    @Query(value = "SELECT g FROM Game g WHERE g.player1 = :player1 AND g.player2 = :player2")
    Game findByPlayer1AndPlayer2(String player1, String player2);

    @Query(value = "SELECT g FROM Game g WHERE g.gameStatus = :status AND (g.player1 = :player OR g.player2 = :player)")
    Game findByParticipatingPlayer(String player, String status);

}
