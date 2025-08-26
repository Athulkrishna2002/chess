package com.sachu.chessgame.controller;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.pieces.Piece;
import com.sachu.chessgame.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/board")
    public String showBoard(Model model) {
        Board board = new Board();
        board.initializeDefaultSetup();
        model.addAttribute("board", board.getGrid());
        return "chessboard";
    }

    @PostMapping("/move")
    public String movePiece(@RequestParam int fromRow,
                            @RequestParam int fromCol,
                            @RequestParam int toRow,
                            @RequestParam int toCol,
                            @RequestParam(required = false) String promotionChoice,
                            Model model) {

        gameService.movePiece(fromRow, fromCol, toRow, toCol, promotionChoice);

        GameState gameState = gameService.getCurrentGame();

        model.addAttribute("board", gameState.getBoard().getGrid());
        model.addAttribute("turn", gameState.getCurrentTurn());
        model.addAttribute("inCheck", gameState.isCheck());
        model.addAttribute("checkmate", gameState.isCheckmate());
        model.addAttribute("stalemate", gameState.isStalemate());

        return "chessboard";  // reload UI
    }

    @GetMapping("/reset")
    public String reset(Model model) {
        gameService.resetGame();
        model.addAttribute("board", gameService.getCurrentGame().getBoard().getGrid());
        return "chessboard"; // reload chessboard.html
    }


}
