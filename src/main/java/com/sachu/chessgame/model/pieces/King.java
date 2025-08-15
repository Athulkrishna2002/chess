package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class King extends Piece{

    public King(PieceColor color) {
        super(color);
    }

    /**
     * Abstract method to validate the piece's move.
     *
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        return false;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♔" : "♚";
    }
}
