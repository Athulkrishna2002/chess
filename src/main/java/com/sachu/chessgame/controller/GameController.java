package com.sachu.chessgame.controller;

import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.enums.PieceColor;
import com.sachu.chessgame.model.pieces.Piece;
import com.sachu.chessgame.services.ComputerPlayerService;
import com.sachu.chessgame.services.GameService;
import com.sachu.chessgame.services.impl.ComputerPlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ComputerPlayerService computerPlayerService;

    @GetMapping("/board")
    public String showBoard(Model model) {
        GameState gameState = gameService.getCurrentGame();
        Board board = new Board();
        board.initializeDefaultSetup();
        model.addAttribute("board", gameState.getBoard().getGrid());
        model.addAttribute("turn", gameState.getCurrentTurn());
        model.addAttribute("inCheck", gameState.isCheck());
        model.addAttribute("checkmate", gameState.isCheckmate());
        model.addAttribute("stalemate", gameState.isStalemate());
        model.addAttribute("gameOver", gameState.isGameOver());
        model.addAttribute("winner", gameState.getWinner());
        return "chessboard";
    }

    @PostMapping(value = "/move", produces = "application/json")
    @ResponseBody
    public Map<String, Object> movePieceAjax(@RequestParam int fromRow,
                                             @RequestParam int fromCol,
                                             @RequestParam int toRow,
                                             @RequestParam int toCol,
                                             @RequestParam(required = false) String promotionChoice) {

        GameState gameState = gameService.movePiece(fromRow, fromCol, toRow, toCol, promotionChoice);

//        // Computer move if game not over
//        if (!gameState.isGameOver()) {
//            gameState = computerPlayerService.makeComputerMove();
//        }

        Map<String, Object> response = new HashMap<>();
        response.put("board", gameState.getBoard().getGrid());
        response.put("turn", gameState.getCurrentTurn());
        response.put("inCheck", gameState.isCheck());
        response.put("checkmate", gameState.isCheckmate());
        response.put("stalemate", gameState.isStalemate());
        response.put("gameOver", gameState.isGameOver());
        response.put("winner", gameState.getWinner());
        response.put("kingRow", gameState.getKingRow());
        response.put("kingCol", gameState.getKingCol());


        return response;
    }



    @GetMapping("/reset")
    public String reset(Model model) {
        gameService.resetGame();
        model.addAttribute("board", gameService.getCurrentGame().getBoard().getGrid());
        return "chessboard"; // reload chessboard.html
    }


}
