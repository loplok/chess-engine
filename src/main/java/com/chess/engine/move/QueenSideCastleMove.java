package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public final class QueenSideCastleMove extends CastleMove {

    public QueenSideCastleMove(Board board, Piece pieceThatMoved, int destinationOfPiece,
                               final Rook castleRook, final int rookPosition, final int castleRookHeadingTo) {
        super(board, pieceThatMoved, destinationOfPiece, castleRook, rookPosition, castleRookHeadingTo);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof KingSideCastleMove)) {
            return false;
        }
        final KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
        return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove.getCastleRook());
    }


    @Override
    public String toString() {
        return "0-0-0";
    }
}
