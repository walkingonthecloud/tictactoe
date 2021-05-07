package com.tmobile.tictactoe.repo;

import com.tmobile.tictactoe.entity.GameTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameTransactionRepo extends JpaRepository<GameTransaction, Long> {

    List<GameTransaction> findByGameId(String gameId);
}
