package com.sachu.chessgame.services;

import com.sachu.chessgame.model.GameState;

public interface GameService {
    GameState getCurrentGame();

    void movePiece(int fromRow, int fromCol, int toRow, int toCol);

}
