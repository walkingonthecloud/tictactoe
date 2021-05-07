package com.tmobile.tictactoe.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Game.class)
@Table(name = "GAME")
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Game {

    @Id
    @GeneratedValue
    private Long id;
    private String gameId;
    private String player1;
    private String player2;
    private String gameStatus;
    private String whooseMove;

    public Game(){};

    public String getWhooseMove() {
        return whooseMove;
    }

    public void setWhooseMove(String whooseMove) {
        this.whooseMove = whooseMove;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("gameId", gameId)
                .append("player1", player1).append("player2", player2)
                .append("status", gameStatus).append("wooseMove", whooseMove).toString();
    }
}
