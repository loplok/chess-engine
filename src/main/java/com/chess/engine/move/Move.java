package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

     protected final Board board;
     protected final Piece pieceThatMoved;
     protected final int destinationOfPiece;
     protected final boolean isFirstMove;

    public Move(final Board board, final Piece pieceThatMoved, final int destinationOfPiece) {
        this.board = board;
        this.pieceThatMoved = pieceThatMoved;
        this.destinationOfPiece = destinationOfPiece;
        this.isFirstMove = pieceThatMoved.isFirstMove();
    }

    public Move(final Board board, final int destinationOfPiece) {
        this.board = board;
        this.destinationOfPiece = destinationOfPiece;
        this.pieceThatMoved = null;
        this.isFirstMove = false;

    }

    @Override
    public int hashCode() {
        final int num = 31;
        int result = 1;

        result = num*result + this.destinationOfPiece;
        result = num*result + this.pieceThatMoved.hashCode();
        result = num*result + this.pieceThatMoved.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(Object toCompare) {
        if(this==toCompare) {
            return true;
        }

        if(!(toCompare instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) toCompare;
        return  getDestinationCoordinate() == otherMove.getDestinationCoordinate()
                && getPieceThatMoved().equals(otherMove.getPieceThatMoved()) && getDestinationCoordinate() == otherMove.getCurrentCoordinate();
    }

    public  int getDestinationCoordinate(){
        return this.destinationOfPiece;
    }

    public Piece getPieceThatMoved() {
        return this.pieceThatMoved;
    }

    public boolean isAttacked() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board getBoard() {
        return this.board;
    }

    public  Board execute() {
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

        builder.setPiece(this.pieceThatMoved.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public int getCurrentCoordinate() {
        return this.pieceThatMoved.getPiecePosition();
    }

    public  boolean isInsertMove() {
        return false;
    }
}
