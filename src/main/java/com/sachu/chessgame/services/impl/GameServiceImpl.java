package com.sachu.chessgame.services.impl;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;
import com.sachu.chessgame.model.pieces.King;
import com.sachu.chessgame.model.pieces.Pawn;
import com.sachu.chessgame.model.pieces.Piece;
import com.sachu.chessgame.model.pieces.Rook;
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
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol, String promotionChoice) {
        Board board = gameState.getBoard();
        Piece piece = board.getPieceAt(startRow, startCol);

        if (piece == null) return false; // no piece selected

        // Check turn
        if (!piece.getColor().name().equals(gameState.getCurrentTurn())) return false;

        // Handle Castling
        if (piece instanceof King && Math.abs(endCol - startCol) == 2 && startRow == endRow) {
            return tryCastling((King) piece, startRow, startCol, endRow, endCol);
        }

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

        // Perform the move
        board.setPieceAt(endRow, endCol, piece);
        board.setPieceAt(startRow, startCol, null);

        // Pawn Promotion check
        if (piece instanceof Pawn) {
            if ((piece.getColor() == PieceColor.WHITE && endRow == 0) ||
                    (piece.getColor() == PieceColor.BLACK && endRow == 7)) {
                // Auto promote to Queen
                Piece promoted;
                switch (promotionChoice != null ? promotionChoice.toUpperCase() : "QUEEN") {
                    case "ROOK":
                        promoted = new com.sachu.chessgame.model.pieces.Rook(piece.getColor());
                        break;
                    case "BISHOP":
                        promoted = new com.sachu.chessgame.model.pieces.Bishop(piece.getColor());
                        break;
                    case "KNIGHT":
                        promoted = new com.sachu.chessgame.model.pieces.Knight(piece.getColor());
                        break;
                    default:
                        promoted = new com.sachu.chessgame.model.pieces.Queen(piece.getColor());
                }
                board.setPieceAt(endRow, endCol, promoted);
            }
        }

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

    private boolean tryCastling(King king, int fromRow, int fromCol, int toRow, int toCol) {
        if (king.hasMoved()) return false;

        if (isInCheck(king.getColor())) return false; // king in check -> no castling

        Board board = gameState.getBoard();

        // Kingside castling
        if (toCol == fromCol + 2) {
            Piece rook = board.getPieceAt(fromRow, 7);
            if (!(rook instanceof Rook) || ((Rook) rook).hasMoved()) return false;

            // Empty squares check
            if (board.getPieceAt(fromRow, 5) != null || board.getPieceAt(fromRow, 6) != null) return false;

            // Perform castling
            board.setPieceAt(fromRow, fromCol, null);
            board.setPieceAt(toRow, toCol, king);
            king.setHasMoved(true);

            board.setPieceAt(fromRow, 7, null);
            board.setPieceAt(fromRow, 5, rook);
            ((Rook) rook).setHasMoved(true);

            switchTurn();
            return true;
        }

        // Queenside castling (long)
        if (toCol == fromCol - 2) {
            Piece rook = board.getPieceAt(fromRow, 0);
            if (!(rook instanceof Rook) || ((Rook) rook).hasMoved()) return false;

            if (board.getPieceAt(fromRow, 1) != null || board.getPieceAt(fromRow, 2) != null || board.getPieceAt(fromRow, 3) != null)
                return false;

            if (isSquareAttacked(board, gameState, fromRow, 2, king.getColor()) ||
                    isSquareAttacked(board, gameState, fromRow, 3, king.getColor())) {
                // castling is not allowed
            }

            // Perform castling
            board.setPieceAt(fromRow, fromCol, null);
            board.setPieceAt(toRow, toCol, king);
            king.setHasMoved(true);

            board.setPieceAt(fromRow, 0, null);
            board.setPieceAt(fromRow, 3, rook);
            ((Rook) rook).setHasMoved(true);

            switchTurn();
            return true;
        }

        return false;
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
