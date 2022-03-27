package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Piece;

public class InsertMove extends Move {
    public InsertMove(Board board, Piece piece, int piecePos) {
        super(board, piece, piecePos);
        piece.setIsTaken(false);
    }

    @Override
    public  Board execute() {
        final Builder builder = new Builder();


        for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
            builder.setPiece(piece);
        }

        for (final Piece piece:
                this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.pieceThatMoved.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    @Override
    public boolean isInsertMove() {
        return true;
    }

    @Override
    public boolean isAttacked() {
        return false;
    }
}
