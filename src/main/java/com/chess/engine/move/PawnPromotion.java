package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public class PawnPromotion extends Move {

    final Move decoratedMove;
    final Pawn promotedPawn;

    public PawnPromotion(final Move decoratedMove) {
        super(decoratedMove.getBoard(), decoratedMove.getPieceThatMoved(), decoratedMove.getDestinationCoordinate());
        this.decoratedMove = decoratedMove;
        this.promotedPawn = (Pawn) decoratedMove.getPieceThatMoved();
    }

    @Override
    public int hashCode() {
        return decoratedMove.hashCode() + (31* promotedPawn.hashCode());
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PawnPromotion && (super.equals(other));
    }

    @Override
    public Board execute() {
        final Board pawnMovedB = this.decoratedMove.execute();
        final Builder builder = new Builder();
        for (final Piece piece: pawnMovedB.currentPlayer().getActivePieces()) {
            if(!this.promotedPawn.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for(final Piece piece: pawnMovedB.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
        builder.setMoveMaker(pawnMovedB.currentPlayer().getAlliance());
        return builder.build();
    }

    @Override
    public boolean isAttacked() {
        return this.decoratedMove.isAttacked();
    }

    @Override
    public Piece getAttackedPiece() {
        return this.decoratedMove.getAttackedPiece();
    }

    @Override
    public String toString() {
        return "";
    }
}
