package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Queen extends Piece{

    public Queen(PieceColor color) {
        super(color);
    }

    /**
     * Abstract method to validate the piece's move.
     *
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     * @param board
     */
    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Move like a rook (straight) or bishop (diagonal)
        if (rowDiff == colDiff || startRow == endRow || startCol == endCol) {
            return true;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♕" : "♛";
    }
}
