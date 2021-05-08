package com.tmobile.tictactoe.api;

import com.tmobile.tictactoe.dto.CreateGameRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveResponseDTO;
import com.tmobile.tictactoe.entity.Game;
import com.tmobile.tictactoe.entity.GameTransaction;

import java.util.List;

public interface GameTransactionService {

    public Game createGame(CreateGameRequestDTO dto);

    public Game concedeGame(String player);

    public CreateMoveResponseDTO createMove(CreateMoveRequestDTO moveRequest);

    public List<Game> listGames();

    public List<GameTransaction> findGameTransactionsByGameId(String gameId);

}
