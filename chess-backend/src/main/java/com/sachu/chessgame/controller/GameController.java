package com.sachu.chessgame.controller;

import com.sachu.chessgame.dto.GameStateResponse;
import com.sachu.chessgame.dto.MoveRequest;
import com.sachu.chessgame.dto.MoveResponse;
import com.sachu.chessgame.mapper.GameStateMapper;
import com.sachu.chessgame.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameStateMapper gameStateMapper;

    @GetMapping
    public ResponseEntity<GameStateResponse> getGame() {
        return ResponseEntity.ok(gameStateMapper.toResponse(gameService.getCurrentGame()));
    }

    @PostMapping("/move")
    public ResponseEntity<MoveResponse> move(@RequestBody MoveRequest request) {
        boolean ok = gameService.movePiece(
                request.fromRow(),
                request.fromCol(),
                request.toRow(),
                request.toCol(),
                request.promotionChoice()
        );
        GameStateResponse state = gameStateMapper.toResponse(gameService.getCurrentGame());
        String message = ok ? "Move accepted" : "Invalid move";
        return ResponseEntity.ok(new MoveResponse(ok, message, state));
    }

    @PostMapping("/reset")
    public ResponseEntity<GameStateResponse> reset() {
        gameService.resetGame();
        return ResponseEntity.ok(gameStateMapper.toResponse(gameService.getCurrentGame()));
    }
}
