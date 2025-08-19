package com.sachu.chessgame.services.impl;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;
import com.sachu.chessgame.model.pieces.Pawn;
import com.sachu.chessgame.model.pieces.Piece;
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
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        Board board = gameState.getBoard();
        Piece piece = board.getPieceAt(startRow, startCol);

        if (piece == null) return false; // no piece selected

        // Check turn
        if (!piece.getColor().name().equals(gameState.getCurrentTurn())) return false;

        // En Passant handling
        if (piece instanceof Pawn) {
            if (handleEnPassant((Pawn) piece, startRow, startCol, endRow, endCol)) {
                switchTurn();
                return true;
            }
        }

        // Normal move validation
        if (!piece.isValidMove(startRow, startCol, endRow, endCol, board, gameState)) return false;

        // Prevent capturing own piece
        Piece target = board.getPieceAt(endRow, endCol);
        if (target != null && target.getColor() == piece.getColor()) return false;

        // Path blocking check (only for rook, bishop, queen)
        if (requiresClearPath(piece) && isPathBlocked(piece, startRow, startCol, endRow, endCol)) {
            return false; // path blocked
        }

        // ✅ Perform the move
        board.setPieceAt(endRow, endCol, piece);
        board.setPieceAt(startRow, startCol, null);

        // Update last move
        gameState.setLastMoveFromRow(startRow);
        gameState.setLastMoveFromCol(startCol);
        gameState.setLastMoveToRow(endRow);
        gameState.setLastMoveToCol(endCol);

        // Switch turn
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

            // ✅ Capture the pawn en passant
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

        // 🚨 Loop until just before destination
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



}
