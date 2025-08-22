package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class King extends Piece{

    public King(PieceColor color) {
        super(color);
    }
        private boolean hasMoved = false;
    /**
     * Abstract method to validate the piece's move.
     *
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board, GameState state) {

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // King can only move 1 square in any direction
        return (rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♔" : "♚";
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }

}
