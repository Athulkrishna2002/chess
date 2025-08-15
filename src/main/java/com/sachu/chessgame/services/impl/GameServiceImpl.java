package com.sachu.chessgame.services.impl;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
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
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece[][] grid = gameState.getBoard().getGrid();
        Piece piece = grid[fromRow][fromCol];

        if(piece == null){
            return;
        }
        if(!piece.getColor().name().equals(gameState.getCurrentTurn())){
            return;
        }
        if (!piece.isValidMove(fromRow, fromCol, toRow, toCol)){
            return;
        }
        if (isPathBlocked(piece, fromRow, fromCol, toRow, toCol)) {
            return;
        }


        grid[toRow][toCol] = piece;
        grid[fromRow][fromCol] = null;

        gameState.setCurrentTurn(
                gameState.getCurrentTurn().equals("WHITE")? "BLACK" : "WHITE"
        );
    }

    private boolean isPathBlocked(Piece piece, int startRow, int startCol, int endRow, int endCol) {
        int rowDir = Integer.compare(endRow, startRow);
        int colDir = Integer.compare(endCol, startCol);

        int r = startRow + rowDir;
        int c = startCol + colDir;

        Piece[][] grid = gameState.getBoard().getGrid();
        while(r != endRow  || c != endCol){
            if(grid[r][c] != null){
                return true;
            }
            r += rowDir;
            c += colDir;
        }
        return false;
    }

    public void resetGame() {
        Board board = new Board();
        board.initializeDefaultSetup();
        this.gameState = new GameState(UUID.randomUUID().toString(), board, "WHITE", false, false, false, false);
    }


}
