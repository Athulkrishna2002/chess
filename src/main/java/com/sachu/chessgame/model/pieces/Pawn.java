package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        // Placeholder
        return true;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♙" : "♟";
    }
}
