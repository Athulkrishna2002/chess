package com.sachu.chessgame.dto;

public record MoveRequest(
        int fromRow,
        int fromCol,
        int toRow,
        int toCol,
        String promotionChoice
) {}
