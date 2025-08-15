package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;
import com.sachu.chessgame.model.Board;

public abstract class Piece {
    protected PieceColor color;
    protected PieceType type;

    public Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    /**
     * Abstract method to validate the piece's move.
     */
    public abstract boolean isValidMove(int startRow, int startCol, int endRow, int endCol);
    public abstract String getSymbol();  // ← NEW for Unicode display
}
