package com.sachu.chessgame.controller;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.pieces.Piece;
import com.sachu.chessgame.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
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
                            Model model) {
        gameService.movePiece(fromRow, fromCol, toRow, toCol);
        model.addAttribute("board", gameService.getCurrentGame().getBoard().getGrid());
        return "chessboard";
    }



}
