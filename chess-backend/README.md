# Chess Backend

Spring Boot REST API for the chess game.

## Run

```bash
mvn spring-boot:run
```

Server listens on **http://localhost:8080**.

## API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/game` | Current board and turn |
| POST | `/api/game/move` | Make a move (JSON body) |
| POST | `/api/game/reset` | Reset to starting position |

### Move request body

```json
{
  "fromRow": 6,
  "fromCol": 4,
  "toRow": 4,
  "toCol": 4,
  "promotionChoice": "QUEEN"
}
```

CORS is enabled for `http://localhost:5173` (Vite dev server).
