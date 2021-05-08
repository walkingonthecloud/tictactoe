package com.tmobile.tictactoe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TicTacToeErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TicTacToeException.class)
    public ResponseEntity<TicTacToeErrorResponse> handleGlobalErrorsAndSendResponse(Exception ex, WebRequest request) {

        TicTacToeErrorResponse errors = TicTacToeErrorResponse.builder()
        .timestamp(LocalDateTime.now()).error(ex.getMessage()).status(HttpStatus.EXPECTATION_FAILED.value())
                .build();
        return new ResponseEntity<>(errors, HttpStatus.EXPECTATION_FAILED);

    }
}



