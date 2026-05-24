package com.sachu.chessgame.mapper;

import com.sachu.chessgame.dto.GameStateResponse;
import com.sachu.chessgame.dto.PieceDto;
import com.sachu.chessgame.model.Board;
import com.sachu.chessgame.model.GameState;
import com.sachu.chessgame.model.pieces.Piece;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameStateMapper {

    public GameStateResponse toResponse(GameState state) {
        return new GameStateResponse(
                state.getGameId(),
                toBoardDto(state.getBoard()),
                state.getCurrentTurn(),
                state.isCheck(),
                state.isCheckmate(),
                state.isStalemate(),
                state.isGameOver()
        );
    }

    private List<List<PieceDto>> toBoardDto(Board board) {
        List<List<PieceDto>> rows = new ArrayList<>();
        Piece[][] grid = board.getGrid();
        for (int r = 0; r < 8; r++) {
            List<PieceDto> row = new ArrayList<>();
            for (int c = 0; c < 8; c++) {
                Piece piece = grid[r][c];
                row.add(piece == null ? null : new PieceDto(piece.getColor(), piece.getType(), piece.getSymbol()));
            }
            rows.add(row);
        }
        return rows;
    }
}
