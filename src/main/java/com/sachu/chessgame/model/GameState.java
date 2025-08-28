package com.sachu.chessgame.model;

import com.sachu.chessgame.model.pieces.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameState {
    private String gameId;
    private Board board;
    private String currentTurn; // "WHITE" or "BLACK"
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isStalemate;
    private boolean gameOver;
    private boolean draw;
    private String winner;

    // Add for En Passant
    private int lastMoveFromRow;
    private int lastMoveFromCol;
    private int lastMoveToRow;
    private int lastMoveToCol;

    private int kingRow = -1;
    private int kingCol = -1;

    private int halfMoveClock; // For 50-move rule



}
