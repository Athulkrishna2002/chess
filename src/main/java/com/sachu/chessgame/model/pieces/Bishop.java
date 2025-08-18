package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.enums.PieceColor;

public class Bishop extends Piece{

    public Bishop(PieceColor color) {
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

        int rowDiff = Math.abs(endRow - startRow);// Maths.abs() => It’s used to get the absolute (positive) value of a number.
        int colDiff = Math.abs(endCol - startCol);

        return rowDiff == colDiff;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♗" : "♝";
    }
}
