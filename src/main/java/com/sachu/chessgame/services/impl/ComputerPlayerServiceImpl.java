package com.sachu.chessgame.services.impl;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.Move;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.pieces.Piece;
import com.sachu.chessgame.services.ComputerPlayerService;
import com.sachu.chessgame.services.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ComputerPlayerServiceImpl implements ComputerPlayerService {

    private final GameServiceImpl gameService;

    public ComputerPlayerServiceImpl(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @Override
    public GameState makeComputerMove() {
        GameState gameState = gameService.getCurrentGame();
        PieceColor turn = PieceColor.valueOf(gameState.getCurrentTurn());

        // Only move if it's BLACK's turn
        if (turn != PieceColor.BLACK || gameState.isGameOver()) return gameState;

        List<Move> legalMoves = getAllLegalMoves(gameState, turn);
        if (legalMoves.isEmpty()) return gameState; // stalemate or checkmate

        // Randomly pick a legal move
        Move move = legalMoves.get(new Random().nextInt(legalMoves.size()));

        // Execute move via gameService (handles turn switching)
        return gameService.movePiece(move.getFromRow(), move.getFromCol(),
                move.getToRow(), move.getToCol(), null);
    }

    public List<Move> getAllLegalMoves(GameState gameState, PieceColor turn) {
        List<Move> moves = new ArrayList<>();
        Board board = gameState.getBoard();
        Piece[][] grid = board.getGrid();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor() == turn) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (toRow == row && toCol == col) continue; // skip no-op moves

                            if (piece.isValidMove(row, col, toRow, toCol, board, gameState)) {
                                Board simulatedBoard = board.clone();
                                simulatedBoard.movePiece(row, col, toRow, toCol);
                                if (!gameService.isInCheck(turn, simulatedBoard)) {
                                    Move legalMove = new Move(row, col, toRow, toCol);
                                    moves.add(legalMove);
                                    System.out.println("Legal move added: " + row + "," + col + " -> " + toRow + "," + toCol);
                                }
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }


}
