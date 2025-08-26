package com.sachu.chessgame.services;

import com.sachu.chessgame.model.GameState;

public interface GameService {
    GameState getCurrentGame();

    GameState movePiece(int fromRow, int fromCol, int toRow, int toCol, String promotionChoice);

    void resetGame();
}
