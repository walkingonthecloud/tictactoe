package com.tmobile.tictactoe.repo;

import com.tmobile.tictactoe.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Long> {

}
