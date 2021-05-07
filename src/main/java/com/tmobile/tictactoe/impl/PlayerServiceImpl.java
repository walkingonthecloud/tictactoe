package com.tmobile.tictactoe.impl;

import com.tmobile.tictactoe.api.PlayerService;
import com.tmobile.tictactoe.entity.Player;
import com.tmobile.tictactoe.repo.PlayerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    public static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    PlayerRepo playerRepo;

    @Autowired
    public PlayerServiceImpl(){};

    @Override
    public Player registerPlayer(String player) throws Exception {

        logger.info("Request received to register player {}", player);

        Player playerRec = new Player();
        try{
            playerRec.setPlayerName(player);
            playerRec.setDraws(0);
            playerRec.setWins(0);
            playerRec.setGames(0);
            playerRepo.save(playerRec);
            logger.info("Registered player {}", playerRec.toString());
        }
        catch (Exception e)
        {
            logger.error("Exception while registering Player {}", player);
            throw new Exception(e);
        }
        return playerRec;
    }
}
