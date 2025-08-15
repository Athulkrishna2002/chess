package com.sachu.chessgame.model;

import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.pieces.*;
import lombok.Data;

@Data
public class Board {
    private Piece[][] grid;

    public Board() {
        this.grid = new Piece[8][8];
    }

    public Piece getPieceAt(int row, int col) {
        return grid[row][col];
    }

    public void setPieceAt(int row, int col, Piece piece) {
        grid[row][col] = piece;
    }

    public void initializeDefaultSetup() {
        // Pawns
        for(int col = 0; col<8;col++){
            grid[1][col] = new Pawn(PieceColor.BLACK);
            grid[6][col] = new Pawn(PieceColor.WHITE);
        }
        // position should set in constructor
        // Rooks
        grid[0][0] = new Rook(PieceColor.BLACK);
        grid[0][7] = new Rook(PieceColor.BLACK);
        grid[7][0] = new Rook(PieceColor.WHITE);
        grid[7][7] = new Rook(PieceColor.WHITE);

        // Knights
        grid[0][1] = new Knight(PieceColor.BLACK);
        grid[0][6] = new Knight(PieceColor.BLACK);
        grid[7][1] = new Knight(PieceColor.WHITE);
        grid[7][6] = new Knight(PieceColor.WHITE);

        // Bishops
        grid[0][2] = new Bishop(PieceColor.BLACK);
        grid[0][5] = new Bishop(PieceColor.BLACK);
        grid[7][2] = new Bishop(PieceColor.WHITE);
        grid[7][5] = new Bishop(PieceColor.WHITE);

        // Queens
        grid[0][3] = new Queen(PieceColor.BLACK);
        grid[7][3] = new Queen(PieceColor.WHITE);

        // Kings
        grid[0][4] = new King(PieceColor.BLACK);
        grid[7][4] = new King(PieceColor.WHITE);
    }
}
