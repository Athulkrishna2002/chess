package com.sachu.chessgame.services.impl;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;
import com.sachu.chessgame.model.pieces.*;
import com.sachu.chessgame.services.GameService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private GameState gameState;

    public GameServiceImpl() {
        resetGame();
    }

    @Override
    public GameState getCurrentGame() {
        return gameState;
    }

    @Override
    public GameState movePiece(int startRow, int startCol, int endRow, int endCol, String promotionChoice) {
        Board board = gameState.getBoard();
        Piece piece = board.getPieceAt(startRow, startCol);
        if (piece == null) return gameState;

        if (!piece.getColor().name().equals(gameState.getCurrentTurn())) return gameState;

        // Castling
        if (piece instanceof King && Math.abs(endCol - startCol) == 2 && startRow == endRow) {
            if (tryCastling((King) piece, startRow, startCol, endRow, endCol)) {
                return gameState;
            }
            return gameState;
        }

        // En Passant
        if (piece instanceof Pawn && handleEnPassant((Pawn) piece, startRow, startCol, endRow, endCol)) {
            switchTurn();
            return gameState;
        }

        // Normal move validation
        if (!piece.isValidMove(startRow, startCol, endRow, endCol, board, gameState)) return gameState;
        Piece target = board.getPieceAt(endRow, endCol);
        if (target != null && target.getColor() == piece.getColor()) return gameState;
        if (requiresClearPath(piece) && isPathBlocked(piece, startRow, startCol, endRow, endCol)) return gameState;

        // --- SIMULATE MOVE ---
        Piece[][] backupGrid = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(board.getGrid()[r], 0, backupGrid[r], 0, 8);
        }

        board.setPieceAt(endRow, endCol, piece);
        board.setPieceAt(startRow, startCol, null);

        // Reject move if it leaves own king in check
        if (isInCheck(piece.getColor())) {
            board.setGrid(backupGrid);
            return gameState;
        }

        // Pawn promotion
        if (piece instanceof Pawn && ((piece.getColor() == PieceColor.WHITE && endRow == 0) ||
                (piece.getColor() == PieceColor.BLACK && endRow == 7))) {
            Piece promoted;
            switch (promotionChoice != null ? promotionChoice.toUpperCase() : "QUEEN") {
                case "ROOK": promoted = new Rook(piece.getColor()); break;
                case "BISHOP": promoted = new Bishop(piece.getColor()); break;
                case "KNIGHT": promoted = new Knight(piece.getColor()); break;
                default: promoted = new Queen(piece.getColor());
            }
            board.setPieceAt(endRow, endCol, promoted);
        }

        // Update last move
        gameState.setLastMoveFromRow(startRow);
        gameState.setLastMoveFromCol(startCol);
        gameState.setLastMoveToRow(endRow);
        gameState.setLastMoveToCol(endCol);

        // Switch turn
        switchTurn();

        // Update check status for opponent
        PieceColor opponent = gameState.getCurrentTurn().equals("WHITE") ? PieceColor.BLACK : PieceColor.WHITE;
        PieceColor currentTurnColor = gameState.getCurrentTurn().equals("WHITE") ? PieceColor.WHITE : PieceColor.BLACK;
        boolean inCheck = isInCheck(currentTurnColor);
        gameState.setCheck(inCheck);

        if (inCheck) {
            System.out.println(currentTurnColor + " King is in CHECK!");
        }

        return gameState;
    }


    private boolean tryCastling(King king, int fromRow, int fromCol, int toRow, int toCol) {
        Board board = gameState.getBoard();
        if (king.hasMoved() || isInCheck(king.getColor())) return false;

        int step = (toCol > fromCol) ? 1 : -1;
        int rookCol = (step == 1) ? 7 : 0;
        Piece rookPiece = board.getPieceAt(fromRow, rookCol);
        if (!(rookPiece instanceof Rook)) return false;
        Rook rook = (Rook) rookPiece;
        if (rook.hasMoved()) return false;

        // Squares between king and rook must be empty
        for (int c = fromCol + step; c != rookCol; c += step) {
            if (board.getPieceAt(fromRow, c) != null) return false;
        }

        // King cannot move through or land in check
        for (int c = fromCol; c != toCol + step; c += step) {
            if (isSquareAttacked(board, gameState, fromRow, c, king.getColor())) return false;
        }

        // Perform castling
        board.setPieceAt(fromRow, fromCol, null);
        board.setPieceAt(toRow, toCol, king);
        king.setHasMoved(true);

        int rookTargetCol = (step == 1) ? (toCol - 1) : (toCol + 1);
        board.setPieceAt(fromRow, rookCol, null);
        board.setPieceAt(fromRow, rookTargetCol, rook);
        rook.setHasMoved(true);

        switchTurn();
        return true;
    }


    private void switchTurn() {
        if ("WHITE".equals(gameState.getCurrentTurn())) {
            gameState.setCurrentTurn("BLACK");
        } else {
            gameState.setCurrentTurn("WHITE");
        }
    }

    private boolean handleEnPassant(Pawn pawn, int startRow, int startCol, int endRow, int endCol) {
        int direction = (pawn.getColor().name().equals("WHITE")) ? -1 : 1;
        int lastToRow = gameState.getLastMoveToRow();
        int lastToCol = gameState.getLastMoveToCol();
        int lastFromRow = gameState.getLastMoveFromRow();
        int lastFromCol = gameState.getLastMoveFromCol();

        Piece lastMoved = null;
        if (lastToRow != -1 && lastToCol != -1) {
            lastMoved = gameState.getBoard().getPieceAt(lastToRow, lastToCol);
        }

        // En Passant: Pawn moved 2 steps last move and is adjacent
        if (lastMoved instanceof Pawn
                && Math.abs(lastToRow - lastFromRow) == 2
                && lastToRow == startRow
                && Math.abs(lastToCol - startCol) == 1
                && endRow == startRow + direction
                && endCol == lastToCol) {

            // Capture the pawn en passant
            gameState.getBoard().setPieceAt(endRow, endCol, pawn);
            gameState.getBoard().setPieceAt(startRow, startCol, null);
            gameState.getBoard().setPieceAt(lastToRow, lastToCol, null);

            // Update last move
            gameState.setLastMoveFromRow(startRow);
            gameState.setLastMoveFromCol(startCol);
            gameState.setLastMoveToRow(endRow);
            gameState.setLastMoveToCol(endCol);

            return true;
        }

        return false;
    }


    private boolean requiresClearPath(Piece piece) {
        switch (piece.getType()) {
            case ROOK:
            case BISHOP:
            case QUEEN:
                return true;
            default:
                return false; // pawns, knights, king don't need full path check
        }
    }


    private boolean isPathBlocked(Piece piece, int startRow, int startCol, int endRow, int endCol) {
        Board board = gameState.getBoard();

        int rowDir = Integer.compare(endRow, startRow); // -1, 0, or 1
        int colDir = Integer.compare(endCol, startCol); // -1, 0, or 1

        int row = startRow + rowDir;
        int col = startCol + colDir;

        // Loop until just before destination
        while (row != endRow || col != endCol) {
            if (board.getPieceAt(row, col) != null) {
                return true; // something is in the way
            }
            row += rowDir;
            col += colDir;
        }

        return false; // path is clear
    }


    public void resetGame() {
        Board board = new Board();
        board.initializeDefaultSetup();

        GameState state = new GameState();
        state.setGameId(UUID.randomUUID().toString());
        state.setBoard(board);
        state.setCurrentTurn("WHITE");
        state.setCheck(false);
        state.setCheckmate(false);
        state.setStalemate(false);
        state.setGameOver(false);

        this.gameState = state;
    }



    private boolean isInCheck(PieceColor color) {
        Board board = gameState.getBoard();
        Piece[][] grid = board.getGrid();

        // Find the king
        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = grid[r][c];
                if (piece instanceof King && piece.getColor() == color) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        }

        if (kingRow == -1) {
            // should never happen if board is valid
            return false;
        }

        // Check if any opponent piece can attack king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece attacker = grid[r][c];
                if (attacker != null && attacker.getColor() != color) {
                    if (attacker.isValidMove(r, c, kingRow, kingCol, board, gameState)) {
                        if (!isPathBlocked(attacker, r, c, kingRow, kingCol)) {
                            return true; // king is attacked
                        }
                    }
                }
            }
        }

        return false; // no attacks found
    }

    public boolean isSquareAttacked(Board board, GameState state, int row, int col, PieceColor defenderColor) {
        Piece[][] grid = board.getGrid();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece attacker = grid[r][c];
                if (attacker != null && attacker.getColor() != defenderColor) {
                    if (attacker.isValidMove(r, c, row, col, board, state)) {
                        if (!isPathBlocked(attacker, r, c, row, col)) {
                            return true; // square is under attack
                        }
                    }
                }
            }
        }
        return false;
    }

}
