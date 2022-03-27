package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public final class MajorAttackMove extends AttackingMove {

    public MajorAttackMove(final Board board, final Piece pieceThatMoved, final int destinationOfPiece, final Piece attackedPiece) {
        super(board, pieceThatMoved, destinationOfPiece, attackedPiece);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PawnAttackMove && super.equals(other);
    }

    @Override
    public String toString() {
        return pieceThatMoved.getPieceType() + "x" + BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
    }

}
