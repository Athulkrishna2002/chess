package com.sachu.chessgame.dto;

import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.enums.PieceType;

public record PieceDto(PieceColor color, PieceType type, String symbol) {}
