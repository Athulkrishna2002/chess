package com.sachu.chessgame.dto;

import java.util.List;

public record GameStateResponse(
        String gameId,
        List<List<PieceDto>> board,
        String currentTurn,
        boolean inCheck,
        boolean checkmate,
        boolean stalemate,
        boolean gameOver
) {}
