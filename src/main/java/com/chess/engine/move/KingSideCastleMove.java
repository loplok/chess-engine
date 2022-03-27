package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public final class KingSideCastleMove extends CastleMove {

    public KingSideCastleMove(Board board, Piece pieceThatMoved, int destinationOfPiece,
                              final Rook castleRook, final int rookPosition, final int castleRookHeadingTo) {
        super(board, pieceThatMoved, destinationOfPiece, castleRook, rookPosition, castleRookHeadingTo);
    }

    @Override
    public String toString() {
        return "0-0";
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof KingSideCastleMove && super.equals(other);
    }

}
