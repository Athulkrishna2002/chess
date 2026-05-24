import * as THREE from 'three';

/** Flatten GLTF node matrices into mesh geometry (removes nested 90° tilts) */
export function bakeMeshesToGroup(source: THREE.Object3D): THREE.Group {
  const group = new THREE.Group();
  source.updateMatrixWorld(true);

  source.traverse((child) => {
    if (!(child instanceof THREE.Mesh)) return;
    const geom = child.geometry.clone();
    geom.applyMatrix4(child.matrixWorld);
    const srcMat = Array.isArray(child.material)
      ? child.material[0]
      : child.material;
    const mesh = new THREE.Mesh(geom, srcMat);
    mesh.name = child.name;
    mesh.castShadow = true;
    mesh.receiveShadow = true;
    group.add(mesh);
  });

  return group;
}

const UPRIGHT_CANDIDATES: [number, number, number][] = [
  [0, 0, 0],
  [-Math.PI / 2, 0, 0],
  [Math.PI / 2, 0, 0],
  [0, 0, -Math.PI / 2],
  [0, 0, Math.PI / 2],
];

/**
 * Standing pieces are tall on Y and narrow on X/Z.
 * Lying pieces have a large X or Z — use aspect score, not raw Y.
 */
export function applyBestUprightRotation(root: THREE.Object3D): void {
  let best = UPRIGHT_CANDIDATES[0];
  let bestScore = -1;

  for (const [rx, ry, rz] of UPRIGHT_CANDIDATES) {
    const trial = root.clone(true);
    trial.rotation.set(rx, ry, rz);
    trial.updateMatrixWorld(true);
    const size = new THREE.Box3().setFromObject(trial).getSize(new THREE.Vector3());
    const footprint = Math.max(size.x, size.z, 0.001);
    const score = size.y / footprint;
    if (score > bestScore) {
      bestScore = score;
      best = [rx, ry, rz];
    }
  }

  root.rotation.set(...best);
}

export function centerGroup(root: THREE.Object3D): void {
  root.updateMatrixWorld(true);
  const center = new THREE.Box3()
    .setFromObject(root)
    .getCenter(new THREE.Vector3());
  root.position.sub(center);
}

export function placeOnSquare(root: THREE.Object3D, squareTop: number): void {
  root.updateMatrixWorld(true);
  const box = new THREE.Box3().setFromObject(root);
  root.position.x -= (box.min.x + box.max.x) / 2;
  root.position.z -= (box.min.z + box.max.z) / 2;
  root.position.y -= box.min.y;
  root.position.y += squareTop;
}

export function scaleToHeight(root: THREE.Object3D, targetHeight: number): void {
  root.updateMatrixWorld(true);
  const box = new THREE.Box3().setFromObject(root);
  const height = box.max.y - box.min.y;
  if (height > 0.001) {
    root.scale.setScalar(targetHeight / height);
  }
}
