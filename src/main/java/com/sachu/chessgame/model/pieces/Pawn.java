package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board) {
        int direction = (this.color == PieceColor.WHITE) ? -1 : 1; // white goes up, black goes down
        int rowDiff = endRow - startRow;
        int colDiff = Math.abs(endCol - startCol);

        Piece target = board.getGrid()[endRow][endCol];

        // ✅ Forward move (1 step, must be empty)
        if (colDiff == 0 && rowDiff == direction && target == null) {
            return true;
        }

        // ✅ First move (2 steps forward, must be empty)
        if (colDiff == 0 && rowDiff == 2 * direction && target == null) {
            int startRowForPawn = (color == PieceColor.WHITE) ? 6 : 1;
            if (startRow == startRowForPawn && board.getGrid()[startRow + direction][startCol] == null) {
                return true;
            }
        }

        // ✅ Diagonal capture (must contain opponent piece)
        if (colDiff == 1 && rowDiff == direction && target != null && target.getColor() != this.color) {
            return true;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♙" : "♟";
    }
}
