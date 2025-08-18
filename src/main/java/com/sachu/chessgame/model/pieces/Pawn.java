package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        int direction = (this.color == PieceColor.WHITE) ? -1 : 1;
        int rowDiff = endRow - startRow;
        int colDiff = endCol - startCol;

        // Forward move (1 step)
        if (colDiff == 0 && rowDiff == direction) {
            return true;
        }

        // First move (2 steps forward)
        if (colDiff == 0 && rowDiff == 2 * direction) {
            if ((this.color == PieceColor.WHITE && startRow == 6) ||
                    (this.color == PieceColor.BLACK && startRow == 1)) {
                return true;
            }
        }

        // Capture diagonally
        if (colDiff == 1 && rowDiff == direction) {
            return true;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♙" : "♟";
    }
}
