package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Knight extends Piece{

    public Knight(PieceColor color) {

        super(color);
        this.type = PieceType.KNIGHT;
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
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board, GameState state) {

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        //Knight moves in "L" shape (2 + 1 or 1 + 2)
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♘" : "♞";
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }

}
