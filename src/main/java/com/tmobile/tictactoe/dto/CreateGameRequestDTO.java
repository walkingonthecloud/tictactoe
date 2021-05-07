package com.tmobile.tictactoe.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
@Data
@Builder
public class CreateGameRequestDTO implements Serializable {

    private String player1;
    private String player2;

}
