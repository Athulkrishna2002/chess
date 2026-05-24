import type { GameStateResponse, MoveRequest, MoveResponse } from '../types/chess';

const API_BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...init?.headers },
    ...init,
  });
  if (!response.ok) {
    throw new Error(`API error ${response.status}: ${response.statusText}`);
  }
  return response.json() as Promise<T>;
}

export function fetchGame(): Promise<GameStateResponse> {
  return request<GameStateResponse>('/api/game');
}

export function postMove(body: MoveRequest): Promise<MoveResponse> {
  return request<MoveResponse>('/api/game/move', {
    method: 'POST',
    body: JSON.stringify(body),
  });
}

export function resetGame(): Promise<GameStateResponse> {
  return request<GameStateResponse>('/api/game/reset', { method: 'POST' });
}
