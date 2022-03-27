package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public class PawnAttackMove extends AttackingMove {

    public PawnAttackMove(Board board, Piece pieceThatMoved, int destinationOfPiece, Piece attackedPiece) {
        super(board, pieceThatMoved, destinationOfPiece, attackedPiece);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PawnAttackMove && super.equals(other);
    }

    @Override
    public String toString() {
        return BoardUtils.getPositionAtCoordinate(this.pieceThatMoved.getPiecePosition()).substring(0,1) +
                "x" + BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
    }

}
