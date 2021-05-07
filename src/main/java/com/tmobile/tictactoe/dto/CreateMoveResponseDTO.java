package com.tmobile.tictactoe.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
@Data
@Builder
public class CreateMoveResponseDTO implements Serializable {
    private String gameId;
    private String player;
    private String move;
    private String result;
}
