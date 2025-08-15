package com.sachu.chessgame.model;

import com.sachu.chessgame.model.pieces.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    private String gameId;
    private Board board;
    private String currentTurn; // "WHITE" or "BLACK"
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isStalemate;
    private boolean gameOver;
}
