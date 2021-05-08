package com.tmobile.tictactoe.impl;

import com.tmobile.tictactoe.api.GameTransactionService;
import com.tmobile.tictactoe.dto.CreateGameRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveResponseDTO;
import com.tmobile.tictactoe.entity.Game;
import com.tmobile.tictactoe.entity.GameTransaction;
import com.tmobile.tictactoe.exception.TicTacToeException;
import com.tmobile.tictactoe.repo.GameRepo;
import com.tmobile.tictactoe.repo.GameTransactionRepo;
import com.tmobile.tictactoe.util.GameStatus;
import lombok.Builder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameTransactionServiceImpl implements GameTransactionService {

    public static final Logger logger = LoggerFactory.getLogger(GameTransactionServiceImpl.class);

    @Autowired
    GameTransactionRepo gameTransactionRepo;

    @Autowired
    GameRepo gameRepo;

    @Autowired
    public GameTransactionServiceImpl(){};

    @Override
    @Transactional
    public Game createGame(CreateGameRequestDTO dto) {

        logger.info("Request received to create a game with details {}", dto);

        Game game = gameRepo.findByPlayer1AndPlayer2(dto.getPlayer1(), dto.getPlayer2());
        if (Objects.nonNull(game) && game.getGameStatus().equals(GameStatus.IN_PROGRESS.name()))
        {
            logger.error("Game already in progress b/w players: {}", game);
            throw new TicTacToeException("Game already in progress b/w players: " + game.toString());
        }

        game = new Game();
        try{
            game.setPlayer1(dto.getPlayer1());
            game.setPlayer2(dto.getPlayer2());
            game.setGameId(UUID.randomUUID().toString());
            game.setGameStatus(GameStatus.IN_PROGRESS.name());
            game.setWhooseMove(StringUtils.EMPTY);
            gameRepo.save(game);
            logger.info("Created Game with details {}", dto);
        }
        catch (Exception e)
        {
            logger.error("Exception while creating game {}", game);
            throw new TicTacToeException(e.getMessage());
        }
        return game;
    }

    @Override
    public Game concedeGame(String player) {
        logger.info("Request received to concede game from {}", player);

        Game game = gameRepo.findByParticipatingPlayer(player, GameStatus.IN_PROGRESS.name());
        if (Objects.isNull(game))
        {
            logger.error("No game in-progress for player: {}", player);
            throw new TicTacToeException("No game in-progress for player: " + player);
        }

        try{
            game.setGameStatus(GameStatus.ABORTED.name());
            gameRepo.save(game);
            logger.info("Conceded Game by player {}", player);
        }
        catch (Exception e)
        {
            logger.error("Exception while conceding game for player {}", player);
            throw new TicTacToeException(e.getMessage());
        }
        return game;
    }

    @Override
    public List<Game> listGames() {
        logger.info("List Games...");
        return gameRepo.findAll();
    }

    @Override
    public List<GameTransaction> findGameTransactionsByGameId(String gameId)
    {
        logger.info("Finding game transactions by gameId: {}", gameId);
        return gameTransactionRepo.findByGameId(gameId);
    }

    @Override
    public CreateMoveResponseDTO createMove(CreateMoveRequestDTO moveRequest) {
        logger.info("Request received to move with details {}", moveRequest);

        Game game = gameRepo.findByParticipatingPlayer(moveRequest.getPlayer(), GameStatus.IN_PROGRESS.name());
        if (Objects.isNull(game))
        {
            logger.error("No game in-progress for player {}, cannot make the move", moveRequest.getPlayer());
            throw new TicTacToeException("No game in-progress, cannot make the move for player: " + moveRequest.getPlayer());
        }

        if (!game.getWhooseMove().equals(StringUtils.EMPTY) && !game.getWhooseMove().equals(moveRequest.getPlayer()))
        {
            logger.error("It is not player {}'s move", moveRequest.getPlayer());
            throw new TicTacToeException("It is not the turn of player:" + moveRequest.getPlayer());
        }

        List<GameTransaction> transactions = gameTransactionRepo.findByGameId(moveRequest.getGameId());
        logger.info("Transactions for the game {} are: {}", moveRequest.getGameId(), transactions.toString());

        transactions = transactions.stream()
                .sorted(Comparator.comparing(GameTransaction::getTime).reversed())
                .collect(Collectors.toList());

        validateMove(transactions, moveRequest);

        long countOfMoves = transactions.stream()
                .filter(tran -> tran.getPlayer().equals(moveRequest.getPlayer()))
                .count();
        logger.info("Count of transactions so far for the game {} are: {}", moveRequest.getGameId(), countOfMoves);
        if (countOfMoves == 3)
        {
            logger.error("Player {} already completed 3 moves", moveRequest.getPlayer());
            throw new TicTacToeException("Already 3 moves completed by the player: " + moveRequest.getPlayer());
        }

        if (!CollectionUtils.isEmpty(transactions)) {
            GameTransaction lastTransaction = transactions.get(0);
            if (lastTransaction.getPlayer().equals(moveRequest.getPlayer())) {
                logger.error("It is not now the turn of player: {}", moveRequest.getPlayer());
                throw new TicTacToeException("It is not now the turn of player: " + moveRequest.getPlayer());
            }
        }

        GameTransaction gameTransaction = new GameTransaction();
        gameTransaction.setMove(moveRequest.getMove());
        gameTransaction.setGameId(moveRequest.getGameId());
        gameTransaction.setPlayer(moveRequest.getPlayer());
        gameTransaction.setTime(LocalDateTime.now());
        gameTransactionRepo.save(gameTransaction);
        logger.info("Successfully created move for player {}", moveRequest.getPlayer());

        CreateMoveResponseDTO responseDTO = CreateMoveResponseDTO.builder().gameId(moveRequest.getGameId())
                .player(moveRequest.getPlayer()).move(moveRequest.getMove()).result(evaluateWinnerAfterMove(moveRequest)).build();
        logger.info("Response DTO for move is {}", responseDTO);

        if (responseDTO.getResult().equals("Won"))
        {
            logger.info("Player {} won, updating game to ENDED", moveRequest.getPlayer());
            game.setGameStatus(GameStatus.ENDED.name());
            gameRepo.save(game);
        }
        else
        {
            logger.info("No clear winner yet, updating whooseMove in the game.");
            String whooseTurnNext = (moveRequest.getPlayer().equals(game.getPlayer1())) ? game.getPlayer2() : game.getPlayer1();
            game.setWhooseMove(whooseTurnNext);
            gameRepo.save(game);
            logger.info("Next move should be done by {}", whooseTurnNext);
        }

        return responseDTO;
    }

    private void validateMove(List<GameTransaction> transactions, CreateMoveRequestDTO dto) {
        List<String> distincMoves = transactions.stream().map(GameTransaction::getMove).distinct().collect(Collectors.toList());
        if (distincMoves.contains(dto.getMove()))
        {
            logger.error("Move request {} is invalid. Position not free.", dto.toString());
            throw new TicTacToeException("Position not free. Move request " + dto.getMove() + " not valid");
        }
        else
            logger.info("Move request {} is Valid. Proceeding to persist...", dto.toString());
    }

    private String evaluateWinnerAfterMove(CreateMoveRequestDTO dto)
    {
        logger.info("Evaluating winner after moveRequest {}", dto);

        List<GameTransaction> transactions = gameTransactionRepo.findByGameId(dto.getGameId());

        List<String> distincMovesByPlayer = transactions.stream()
                .filter(tran -> tran.getPlayer().equals(dto.getPlayer()))
                .map(GameTransaction::getMove)
                .distinct().sorted()
                .collect(Collectors.toList());

        if (distincMovesByPlayer.size() < 3)
        {
            logger.info("Player {} has not completed 3 moves yet in the game {}.", dto.getPlayer(), dto.getGameId());
            return StringUtils.EMPTY;
        }

        if (distincMovesByPlayer.get(0).equals("1,1") && distincMovesByPlayer.get(1).equals("2,2") && distincMovesByPlayer.get(2).equals("3,3"))
        {
            logger.info("Player {} has won in the game {}.", dto.getPlayer(), dto.getGameId());
            return "Won";
        }

        if (distincMovesByPlayer.get(0).equals("1,3") && distincMovesByPlayer.get(1).equals("2,2") && distincMovesByPlayer.get(2).equals("3,1"))
        {
            logger.info("Player {} has won in the game {}.", dto.getPlayer(), dto.getGameId());
            return "Won";
        }

        boolean won = false;
        Set<String> rows = new HashSet<>();
        Set<String> columns = new HashSet<>();
        for (String move: distincMovesByPlayer)
        {
            String[] currentMove = move.split(",");
            rows.add(currentMove[0]);
            columns.add(currentMove[1]);
        }
        if (rows.size() == 3 && columns.size() == 1 || rows.size() == 1 && columns.size() == 3)
        {
            logger.info("Player {} has won in the game {}.", dto.getPlayer(), dto.getGameId());
            return "Won";
        }
        return StringUtils.EMPTY;
    }
}
