package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.board.BoardUtils.NUM_TILES;

public abstract class Tile  {
    protected final int tileNumber;
    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllEmptyFiles();

    private static Map<Integer,EmptyTile> createAllEmptyFiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();

        for (int i =0; i < NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int tileNumber, final Piece piece) {
        return piece != null ? new OccupiedTile(tileNumber, piece) : EMPTY_TILES.get(tileNumber);
    }



    private Tile(final int tileCoordinate) {
        this.tileNumber = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileNumber() {
        return this.tileNumber;
    }



    public static final class EmptyTile extends Tile{
        EmptyTile(int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }

    }


    public static final class OccupiedTile extends Tile {
        private final Piece piece;

        private OccupiedTile(int coordinate, Piece pieceOnTile) {
            super(coordinate);
            this.piece = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return piece;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }
        }
    }

