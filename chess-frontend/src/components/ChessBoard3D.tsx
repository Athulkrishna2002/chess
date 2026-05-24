import { Canvas } from '@react-three/fiber';
import { ContactShadows, OrbitControls } from '@react-three/drei';
import { Suspense, useCallback, useMemo } from 'react';
import type { GameStateResponse, SquareSelection } from '../types/chess';
import { PieceMesh } from './PieceMesh';

const LIGHT_SQUARE = '#e8d4b0';
const DARK_SQUARE = '#7a5c3e';
const SELECTED = '#ff5252';

interface ChessBoard3DProps {
  game: GameStateResponse;
  selection: SquareSelection;
  onSquareClick: (row: number, col: number) => void;
}

function BoardScene({ game, selection, onSquareClick }: ChessBoard3DProps) {
  const squares = useMemo(() => {
    const items: { row: number; col: number; color: string }[] = [];
    for (let row = 0; row < 8; row++) {
      for (let col = 0; col < 8; col++) {
        const isLight = (row + col) % 2 === 0;
        const selected = selection?.row === row && selection?.col === col;
        items.push({
          row,
          col,
          color: selected ? SELECTED : isLight ? LIGHT_SQUARE : DARK_SQUARE,
        });
      }
    }
    return items;
  }, [selection]);

  const handleClick = useCallback(
    (row: number, col: number) => () => onSquareClick(row, col),
    [onSquareClick],
  );

  return (
    <>
      <ambientLight intensity={0.65} />
      <directionalLight
        position={[8, 14, 6]}
        intensity={1.4}
        castShadow
        shadow-mapSize={[1024, 1024]}
      />
      <directionalLight position={[-6, 8, -4]} intensity={0.35} />
      <ContactShadows
        position={[3.5, 0.01, 3.5]}
        opacity={0.4}
        scale={12}
        blur={2}
        far={4}
      />
      <mesh rotation={[-Math.PI / 2, 0, 0]} position={[3.5, -0.02, 3.5]} receiveShadow>
        <planeGeometry args={[9, 9]} />
        <meshStandardMaterial color="#2d2418" />
      </mesh>

      {squares.map(({ row, col, color }) => (
        <mesh
          key={`sq-${row}-${col}`}
          position={[col, 0, row]}
          onPointerDown={handleClick(row, col)}
          receiveShadow
        >
          <boxGeometry args={[0.98, 0.12, 0.98]} />
          <meshStandardMaterial color={color} />
        </mesh>
      ))}

      {game.board.map((row, rowIndex) =>
        row.map((piece, colIndex) =>
          piece ? (
            <Suspense key={`p-${rowIndex}-${colIndex}`} fallback={null}>
              <PieceMesh
                piece={piece}
                position={[colIndex, 0, rowIndex]}
              />
            </Suspense>
          ) : null,
        ),
      )}

      <OrbitControls
        enablePan
        minPolarAngle={0.2}
        maxPolarAngle={Math.PI / 2.1}
        target={[3.5, 0, 3.5]}
      />
    </>
  );
}

export function ChessBoard3D(props: ChessBoard3DProps) {
  return (
    <div className="board-canvas">
      <Canvas shadows camera={{ position: [5, 8, 9], fov: 45 }}>
        <BoardScene {...props} />
      </Canvas>
    </div>
  );
}
