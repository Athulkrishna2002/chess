package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Rook extends Piece{

    private boolean hasMoved = false;

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
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board, GameState state) {
        if(startRow != endRow && startCol != endCol){
            return false;
        }
        return true;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♖" : "♜";
    }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public Piece clone() {
        return new Rook(this.getColor());
    }


}
