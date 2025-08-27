package com.sachu.chessgame.model.pieces;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board, GameState state) {
        int direction = (this.color == PieceColor.WHITE) ? -1 : 1;
        int rowDiff = endRow - startRow;
        int colDiff = endCol - startCol;

        Piece target = board.getPieceAt(endRow, endCol);

        // Normal forward
        if (colDiff == 0 && rowDiff == direction && target == null) return true;

        // First move (2 steps)
        if (colDiff == 0 && rowDiff == 2 * direction && target == null) {
            int startRowForPawn = (color == PieceColor.WHITE) ? 6 : 1;
            if (startRow == startRowForPawn && board.getPieceAt(startRow + direction, startCol) == null) {
                return true;
            }
        }

        // Normal capture
        if (Math.abs(colDiff) == 1 && rowDiff == direction && target != null && target.getColor() != this.color) {
            return true;
        }

        // ✅ En Passant
        if (state != null) {
            int lastFromRow = state.getLastMoveFromRow();
            int lastFromCol = state.getLastMoveFromCol();
            int lastToRow = state.getLastMoveToRow();
            int lastToCol = state.getLastMoveToCol();

            Piece lastMovedPiece = board.getPieceAt(lastToRow, lastToCol);

            if (lastMovedPiece instanceof Pawn &&
                    Math.abs(lastToRow - lastFromRow) == 2 && // moved 2 squares
                    lastToRow == startRow &&                 // same row as our pawn
                    lastToCol == endCol &&                   // pawn is beside us
                    Math.abs(endCol - startCol) == 1 &&      // diagonal move
                    rowDiff == direction &&                  // moving forward
                    target == null) {                        // target square empty
                return true;
            }
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return color == PieceColor.WHITE ? "♙" : "♟";
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public Piece clone() {
        return new Pawn(this.getColor());
    }


}
