package com.sachu.chessgame.dto;

public record MoveResponse(boolean success, String message, GameStateResponse game) {}
