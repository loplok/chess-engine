package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

public class AttackingMove extends Move {

    final Piece attackedPiece;

    public AttackingMove(Board board, Piece pieceThatMoved, int destinationOfPiece, final Piece attackedPiece) {
        super(board, pieceThatMoved, destinationOfPiece);
        this.attackedPiece = attackedPiece;
    }

    @Override
    public boolean isAttacked() {
        return true;
    }

    @Override
    public Piece getAttackedPiece() {
        return this.attackedPiece;
    }

    @Override
    public int hashCode() {
        return this.attackedPiece.hashCode() + super.hashCode();
    }

    @Override
    public boolean equals(final Object toCompare) {
        if(this==toCompare) {
            return true;
        }

        if(!(toCompare instanceof AttackingMove)) {
            return false;
        }

        final AttackingMove otherAttackMove = (AttackingMove) toCompare;

        return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());

    }
}
