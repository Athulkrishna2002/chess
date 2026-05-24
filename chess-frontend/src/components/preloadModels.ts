import { useGLTF } from '@react-three/drei';
import { PIECE_MODEL_URL, PIECE_TYPES } from '../config/pieceModels';

/** Preload all piece GLTFs (safe to call outside React) */
export function preloadChessModels() {
  for (const type of PIECE_TYPES) {
    useGLTF.preload(PIECE_MODEL_URL[type]);
  }
}

preloadChessModels();
