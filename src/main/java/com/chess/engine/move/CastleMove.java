package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class CastleMove extends Move {
    protected final Rook castleRook;
    protected final int rookPosition;
    protected final int castleRookHeadingTo;

    public CastleMove(Board board, Piece pieceThatMoved, int destinationOfPiece,
                      final Rook castleRook, final int rookPosition, final int castleRookHeadingTo) {
        super(board, pieceThatMoved, destinationOfPiece);
        this.castleRook = castleRook;
        this.rookPosition = rookPosition;
        this.castleRookHeadingTo = castleRookHeadingTo;
    }

    public Rook getCastleRook() {
        return this.castleRook;
    }

    @Override
    public boolean isCastlingMove() {
        return true;
    }

    @Override
    public int hashCode() {
        final int num = 31;
        int result = super.hashCode();

        result = num*result + this.castleRook.hashCode();
        result = num*result + this.castleRookHeadingTo;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if(!(other instanceof CastleMove)) {
            return false;
        }
        final CastleMove otherCastleMove = (CastleMove) other;
        return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
    }

    @Override
    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.getAllPieces()) {
            if (!this.pieceThatMoved.equals(piece) && !this.castleRook.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        builder.setPiece(this.pieceThatMoved.movePiece(this));
        builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookHeadingTo,false, false));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        builder.setMoveTransition(this);
        return builder.build();
    }
}
