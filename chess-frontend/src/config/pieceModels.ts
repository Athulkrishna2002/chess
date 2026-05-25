import type { PieceType } from '../types/chess';

export const PIECE_MODEL_URL: Record<PieceType, string> = {
  PAWN: '/models/pawn/scene.gltf',
  ROOK: '/models/rook/scene.gltf',
  KNIGHT: '/models/knight/scene.gltf',
  BISHOP: '/models/bishop/wooden_bishop_chess_piece.glb',
  QUEEN: '/models/queen/scene.gltf',
  KING: '/models/king/scene.gltf',
};

export const DEFAULT_PIECE_HEIGHT = 0.70;

export const PIECE_HEIGHT: Record<PieceType, number> = {
  PAWN: DEFAULT_PIECE_HEIGHT,
  ROOK: 0.80,
  KNIGHT: 0.86,
  BISHOP: 0.96,
  QUEEN: 0.96,
  KING: 0.98,
};

/**
 * Fixed XYZ euler (degrees in rad) — matches chess3d reference loader.
 * Bishop: GLTF node matrix already stands it up → no extra rotation.
 * Knight/Rook: model length is along Z → rotate -90° on X.
 * Knight facing uses Z rotation (chess3d: set(-90°X, 0, faceZ)).
 */
export const PIECE_ROTATION: Record<
  PieceType,
  { x: number; y: number; z: number; faceY: boolean }
> = {
  PAWN: { x: 0, y: 0, z: 0, faceY: false },
  BISHOP: { x: 0, y: 0, z: 0, faceY: false },
  QUEEN: { x: 0, y: 0, z: 0, faceY: false },
  KING: { x: 0, y: 0, z: 0, faceY: false },
  ROOK: { x: -Math.PI / 2, y: 0, z: 0, faceY: false },
  KNIGHT: { x: -Math.PI / 2, y: 0, z: 0, faceY: true },
};

export const PIECE_TYPES = Object.keys(PIECE_MODEL_URL) as PieceType[];
