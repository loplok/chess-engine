package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public final class PawnJump extends Move {

    public PawnJump(Board board, Piece pieceThatMoved, int destinationOfPiece) {
        super(board, pieceThatMoved, destinationOfPiece);
    }

    @Override
    public String toString() {
        return BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
    }

    @Override
    public Board execute() {
        final Builder builder = new Builder();

        for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
            if(!this.pieceThatMoved.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece:
                this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }



        final Pawn movedPawn = (Pawn) this.pieceThatMoved.movePiece(this);
        builder.setPiece(movedPawn);
        builder.setEnPassant(movedPawn);
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();

    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PawnJump && super.equals(other);
    }
}
