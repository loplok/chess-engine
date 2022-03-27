package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public final class MajorPieceMove extends Move {

    public MajorPieceMove(Board board, Piece pieceThatMoved, int destinationOfPiece) {
        super(board, pieceThatMoved, destinationOfPiece);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof MajorPieceMove && super.equals(other);
    }

    @Override
    public String toString() {
        return pieceThatMoved.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.getDestinationCoordinate());
    }
}