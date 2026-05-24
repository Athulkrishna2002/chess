import { useGLTF } from '@react-three/drei';
import { useMemo } from 'react';
import * as THREE from 'three';
import {
  PIECE_HEIGHT,
  PIECE_MODEL_URL,
  PIECE_ROTATION,
} from '../config/pieceModels';
import type { PieceDto, PieceType } from '../types/chess';
import { centerGroup, placeOnSquare, scaleToHeight } from '../utils/bakeModel';

const WHITE = new THREE.Color('#f5f0e8');
const BLACK = new THREE.Color('#561C24');

const SQUARE_TOP = 0.07;

interface PieceMeshProps {
  piece: PieceDto;
  position: [number, number, number];
}

function resolvePieceType(piece: PieceDto): PieceType {
  if (piece.type && PIECE_MODEL_URL[piece.type]) return piece.type;
  const sym = piece.symbol?.toLowerCase() ?? '';
  if (sym.includes('pawn') || sym === '♙' || sym === '♟') return 'PAWN';
  if (sym.includes('rook') || sym === '♖' || sym === '♜') return 'ROOK';
  if (sym.includes('knight') || sym === '♘' || sym === '♞') return 'KNIGHT';
  if (sym.includes('bishop') || sym === '♗' || sym === '♝') return 'BISHOP';
  if (sym.includes('queen') || sym === '♕' || sym === '♛') return 'QUEEN';
  return 'KING';
}

function applyPieceMaterials(root: THREE.Object3D, piece: PieceDto) {
  const base = piece.color === 'WHITE' ? WHITE : BLACK;

  root.traverse((child) => {
    if (!(child instanceof THREE.Mesh)) return;
    child.castShadow = true;
    child.receiveShadow = true;
    child.renderOrder = 2;
    child.material = new THREE.MeshStandardMaterial({
      color: base.clone(),
      metalness: 0.2,
      roughness: 0.45,
      side: THREE.DoubleSide,
    });
  });
}

function applyPieceRotation(
  root: THREE.Object3D,
  pieceType: PieceType,
  color: PieceDto['color'],
) {
  const { x, y, faceY } = PIECE_ROTATION[pieceType];
  // Face on Z (not Y) — avoids gimbal lock after the -90° X tilt
  const faceZ =
    faceY && color === 'BLACK'
      ? Math.PI / 2
      : faceY && color === 'WHITE'
        ? -Math.PI / 2
        : 0;

  root.rotation.set(x, y, faceZ, 'XYZ');
}

function preparePieceModel(
  scene: THREE.Object3D,
  piece: PieceDto,
  pieceType: PieceType,
): THREE.Object3D {
  const clone = scene.clone(true);
  applyPieceMaterials(clone, { ...piece, type: pieceType });

  centerGroup(clone);
  applyPieceRotation(clone, pieceType, piece.color);

  clone.updateMatrixWorld(true);
  scaleToHeight(clone, PIECE_HEIGHT[pieceType]);
  placeOnSquare(clone, SQUARE_TOP);

  return clone;
}

export function PieceMesh({ piece, position }: PieceMeshProps) {
  const pieceType = resolvePieceType(piece);
  const url = PIECE_MODEL_URL[pieceType];
  const { scene } = useGLTF(url);

  const model = useMemo(
    () => preparePieceModel(scene, piece, pieceType),
    [scene, pieceType, piece.color],
  );

  return (
    <group position={[position[0], 0, position[2]]}>
      <primitive object={model} />
    </group>
  );
}
