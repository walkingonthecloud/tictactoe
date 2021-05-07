package com.tmobile.tictactoe.api;

import com.tmobile.tictactoe.dto.CreateGameRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveResponseDTO;
import com.tmobile.tictactoe.entity.Game;
import com.tmobile.tictactoe.entity.GameTransaction;

public interface GameTransactionService {

    public Game createGame(CreateGameRequestDTO dto) throws Exception;

    public Game concedeGame(String player) throws Exception;

    public CreateMoveResponseDTO createMove(CreateMoveRequestDTO moveRequest) throws Exception;

}
