package com.tmobile.tictactoe.rest;

import com.tmobile.tictactoe.dto.CreateGameRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveRequestDTO;
import com.tmobile.tictactoe.dto.CreateMoveResponseDTO;
import com.tmobile.tictactoe.entity.Game;
import com.tmobile.tictactoe.entity.Player;
import com.tmobile.tictactoe.impl.GameTransactionServiceImpl;
import com.tmobile.tictactoe.impl.PlayerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/game")
public class GameController {

    public static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    PlayerServiceImpl playerService;

    @Autowired
    GameTransactionServiceImpl gameTransactionService;

    @GetMapping(value = "/user/{userId}/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> registerPlayer(@PathVariable (required = true) String userId) throws Exception {
        logger.info("Registering user: {}", userId);
        Player player = playerService.registerPlayer(userId);
        ResponseEntity<Player> responseEntity = new ResponseEntity<>(player, HttpStatus.OK);
        logger.info("Registered user: {} sucess!", userId);
        return responseEntity;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> createGame(@RequestBody CreateGameRequestDTO dto) throws Exception {
        logger.info("Creating game for request: {}", dto);
        Game game = gameTransactionService.createGame(dto);
        ResponseEntity<Game> responseEntity = new ResponseEntity<>(game, HttpStatus.OK);
        logger.info("Created game: {} sucess!", game);
        return responseEntity;
    }

    @GetMapping(value = "/user/{userId}/concede", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> concedeGame(@PathVariable (required = true) String userId) throws Exception {
        logger.info("Conceding user: {}", userId);
        Game game = gameTransactionService.concedeGame(userId);
        ResponseEntity<Game> responseEntity = new ResponseEntity<>(game, HttpStatus.OK);
        logger.info("Concede request by user: {} sucess!", userId);
        return responseEntity;
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateMoveResponseDTO> createGame(@RequestBody CreateMoveRequestDTO dto) throws Exception {
        logger.info("Creating move request: {}", dto);
        CreateMoveResponseDTO responseDTO = gameTransactionService.createMove(dto);
        ResponseEntity<CreateMoveResponseDTO> responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.OK);
        logger.info("Created move request: {} sucess!", responseDTO);
        return responseEntity;
    }

}
