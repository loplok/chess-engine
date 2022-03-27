package com.chess.engine.tile;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.board.BoardUtils.NUM_TILES;

public abstract class Tile  {
    protected final int tileNumber;
    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllEmptyTiles();

    public Tile(final int tileCoordinate) {
        this.tileNumber = tileCoordinate;
    }

    private static Map<Integer,EmptyTile> createAllEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();

        for (int i =0; i < NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int tileNumber, final Piece piece) {
        return piece != null ? new OccupiedTile(tileNumber, piece) : EMPTY_TILES.get(tileNumber);
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileNumber() {
        return this.tileNumber;
    }
}

