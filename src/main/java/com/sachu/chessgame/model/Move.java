package com.sachu.chessgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Move {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
}
