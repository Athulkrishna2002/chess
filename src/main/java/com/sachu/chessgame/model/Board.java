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

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = grid[fromRow][fromCol];
        grid[toRow][toCol] = piece;
        grid[fromRow][fromCol] = null;
    }

    @Override
    public Board clone() {
        Board copy = new Board();
        Piece[][] newGrid = new Piece[8][8];

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece original = this.grid[r][c];
                if (original != null) {
                    newGrid[r][c] = original.clone(); // deep copy of piece
                }
            }
        }

        copy.setGrid(newGrid);
        return copy;
    }


    public void printBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = grid[r][c];
                if (piece == null) {
                    System.out.print(". "); // empty square
                } else {
                    String symbol = piece.getClass().getSimpleName().substring(0, 1);
                    if (piece instanceof Knight) symbol = "N"; // knights use N
                    if (piece instanceof King) symbol = "K";
                    if (piece instanceof Queen) symbol = "Q";
                    if (piece instanceof Rook) symbol = "R";
                    if (piece instanceof Bishop) symbol = "B";
                    if (piece instanceof Pawn) symbol = "P";

                    // lowercase for black, uppercase for white
                    if (piece.getColor() == PieceColor.BLACK) {
                        symbol = symbol.toLowerCase();
                    }
                    System.out.print(symbol + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }



}
