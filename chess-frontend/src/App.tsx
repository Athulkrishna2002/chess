import { useCallback, useEffect, useState } from 'react';
import './App.css';
import { fetchGame, postMove, resetGame } from './api/chessApi';
import { ChessBoard3D } from './components/ChessBoard3D';
import type { GameStateResponse, SquareSelection } from './types/chess';

const PROMOTION_OPTIONS = ['QUEEN', 'ROOK', 'BISHOP', 'KNIGHT'] as const;

function App() {
  const [game, setGame] = useState<GameStateResponse | null>(null);
  const [selection, setSelection] = useState<SquareSelection>(null);
  const [message, setMessage] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [promotionPending, setPromotionPending] = useState<{
    fromRow: number;
    fromCol: number;
    toRow: number;
    toCol: number;
  } | null>(null);

  const loadGame = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const state = await fetchGame();
      setGame(state);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load game');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadGame();
  }, [loadGame]);

  const submitMove = async (
    fromRow: number,
    fromCol: number,
    toRow: number,
    toCol: number,
    promotionChoice?: string,
  ) => {
    try {
      const result = await postMove({
        fromRow,
        fromCol,
        toRow,
        toCol,
        promotionChoice,
      });
      setGame(result.game);
      setMessage(result.message);
      setSelection(null);
    } catch (e) {
      setMessage(e instanceof Error ? e.message : 'Move failed');
    }
  };

  const handleSquareClick = (row: number, col: number) => {
    if (!game) return;

    if (!selection) {
      const piece = game.board[row]?.[col];
      if (!piece) return;
      setSelection({ row, col });
      return;
    }

    const { row: fromRow, col: fromCol } = selection;
    const piece = game.board[fromRow]?.[fromCol];
    if (!piece) {
      setSelection(null);
      return;
    }

    const isWhitePawn = piece.type === 'PAWN' && piece.color === 'WHITE' && row === 0;
    const isBlackPawn = piece.type === 'PAWN' && piece.color === 'BLACK' && row === 7;

    if (isWhitePawn || isBlackPawn) {
      setPromotionPending({ fromRow, fromCol, toRow: row, toCol: col });
      setSelection(null);
      return;
    }

    void submitMove(fromRow, fromCol, row, col);
  };

  const handlePromotion = (choice: string) => {
    if (!promotionPending) return;
    const { fromRow, fromCol, toRow, toCol } = promotionPending;
    setPromotionPending(null);
    void submitMove(fromRow, fromCol, toRow, toCol, choice);
  };

  const handleReset = async () => {
    try {
      const state = await resetGame();
      setGame(state);
      setMessage('Game reset');
      setSelection(null);
      setPromotionPending(null);
    } catch (e) {
      setMessage(e instanceof Error ? e.message : 'Reset failed');
    }
  };

  return (
    <div className="app">
      <header className="header">
        <h1>Chess 3D</h1>
        {game && (
          <div className="status">
            <span className="badge">Turn: {game.currentTurn}</span>
            {game.inCheck && <span className="badge warn">Check!</span>}
            {message && <span className="message">{message}</span>}
          </div>
        )}
        <button type="button" className="btn" onClick={() => void handleReset()}>
          Reset
        </button>
      </header>

      {loading && <p className="hint">Loading board…</p>}
      {error && (
        <p className="error">
          {error}. Start the backend with <code>mvn spring-boot:run</code> in{' '}
          <code>chess-backend</code>.
        </p>
      )}

      {game && !loading && (
        <ChessBoard3D
          game={game}
          selection={selection}
          onSquareClick={handleSquareClick}
        />
      )}

      {promotionPending && (
        <div className="modal-backdrop">
          <div className="modal">
            <p>Promote pawn to:</p>
            <div className="promo-buttons">
              {PROMOTION_OPTIONS.map((opt) => (
                <button
                  key={opt}
                  type="button"
                  className="btn"
                  onClick={() => handlePromotion(opt)}
                >
                  {opt}
                </button>
              ))}
            </div>
          </div>
        </div>
      )}

      <p className="hint">
        Click a piece, then click a destination square. Drag to rotate the board.
      </p>
    </div>
  );
}

export default App;
