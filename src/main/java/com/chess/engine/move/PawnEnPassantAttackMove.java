package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Piece;

public final class PawnEnPassantAttackMove extends PawnAttackMove {

    public PawnEnPassantAttackMove(Board board, Piece pieceThatMoved, int destinationOfPiece, Piece attackedPiece) {
        super(board, pieceThatMoved, destinationOfPiece, attackedPiece);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
    }

    @Override
    public String toString() {
        return BoardUtils.getPositionAtCoordinate(this.pieceThatMoved.getPiecePosition()).substring(0,1) +
                "x" + BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
    }

    @Override
    public Board execute() {
        final Builder builder = new Builder();

        for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
            if(!this.pieceThatMoved.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            if(!piece.equals(this.attackedPiece)) {
                builder.setPiece(piece);
            }
        }
        builder.setPiece(this.pieceThatMoved.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
}
