package com.chess.engine.tile;

import com.chess.engine.pieces.Piece;

public final class OccupiedTile extends Tile {
    private final Piece piece;

    public OccupiedTile(int coordinate, Piece pieceOnTile) {
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
