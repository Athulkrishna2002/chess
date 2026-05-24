import * as THREE from 'three';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { fileURLToPath } from 'url';
import path from 'path';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const modelsDir = path.join(__dirname, '../public/models');

function bakeMeshes(root) {
  const group = new THREE.Group();
  root.updateMatrixWorld(true);
  root.traverse((child) => {
    if (child.isMesh) {
      const geom = child.geometry.clone();
      geom.applyMatrix4(child.matrixWorld);
      const mesh = new THREE.Mesh(geom);
      group.add(mesh);
    }
  });
  return group;
}

function bboxInfo(obj) {
  obj.updateMatrixWorld(true);
  const box = new THREE.Box3().setFromObject(obj);
  const size = box.getSize(new THREE.Vector3());
  return {
    size: { x: +size.x.toFixed(3), y: +size.y.toFixed(3), z: +size.z.toFixed(3) },
    height: +(box.max.y - box.min.y).toFixed(3),
  };
}

const loader = new GLTFLoader();
for (const piece of ['bishop', 'knight', 'pawn', 'king']) {
  const filePath = path.join(modelsDir, piece, 'scene.gltf');
  const gltf = await loader.loadAsync(
    `file:///${filePath.replace(/\\/g, '/')}`,
  );
  const raw = bboxInfo(gltf.scene);
  const baked = bakeMeshes(gltf.scene);
  const bakedBox = bboxInfo(baked);

  const tests = [
    ['baked', baked],
    ['baked -90X', baked.clone().rotateX(-Math.PI / 2)],
    ['baked +90X', baked.clone().rotateX(Math.PI / 2)],
    ['baked -90Z', baked.clone().rotateZ(-Math.PI / 2)],
  ];
  console.log(`\n=== ${piece.toUpperCase()} ===`);
  console.log('raw', raw);
  console.log('baked', bakedBox);
  for (const [name, obj] of tests) {
    if (name === 'baked') continue;
    console.log(name, bboxInfo(obj));
  }
}
