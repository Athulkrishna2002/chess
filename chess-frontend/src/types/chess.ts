export type PieceColor = 'WHITE' | 'BLACK';
export type PieceType =
  | 'PAWN'
  | 'ROOK'
  | 'KNIGHT'
  | 'BISHOP'
  | 'QUEEN'
  | 'KING';

export interface PieceDto {
  color: PieceColor;
  type: PieceType;
  symbol: string;
}

export interface GameStateResponse {
  gameId: string;
  board: (PieceDto | null)[][];
  currentTurn: string;
  inCheck: boolean;
  checkmate: boolean;
  stalemate: boolean;
  gameOver: boolean;
}

export interface MoveResponse {
  success: boolean;
  message: string;
  game: GameStateResponse;
}

export interface MoveRequest {
  fromRow: number;
  fromCol: number;
  toRow: number;
  toCol: number;
  promotionChoice?: string;
}

export type SquareSelection = { row: number; col: number } | null;
