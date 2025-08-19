package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Rook extends Piece{

    public Rook(PieceColor color) {
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
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board) {
        if(startRow != endRow && startCol != endCol){
            return false;
        }
        return true;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♖" : "♜";
    }
}
