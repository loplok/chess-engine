package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public final class  PawnMove extends Move {

    public PawnMove(Board board, Piece pieceThatMoved, int destinationOfPiece) {
        super(board, pieceThatMoved, destinationOfPiece);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PawnMove && super.equals(other);
    }

    @Override
    public String toString() {
        return BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
    }
}
