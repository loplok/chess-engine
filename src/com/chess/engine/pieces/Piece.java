package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    protected boolean isTaken;

    private final int cashedCode;

    Piece(final int piecePosition,
          final Alliance pieceAlliance,
          final PieceType pieceType,
          final boolean isFirstMove, final boolean isTaken) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;
        this.cashedCode = computeHashCode();
        this.isTaken = isTaken;
    }

    // getter and setters for taken boolean of a piece
    public boolean getIsTaken() {
        return this.isTaken;
    }

    public void setIsTaken(boolean taken) {
        this.isTaken = taken;
    }


    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public abstract Piece movePiece(Move move);

    public abstract  Collection<Move> calculateLegalMove(final Board board);

    @Override
    public boolean equals(final Object toCompare) {
        if(this==toCompare) {
            return true;
        }
        if(!(toCompare instanceof Piece)) {
            return false;
        }

        final Piece otherPiece = (Piece) toCompare;
        return piecePosition == otherPiece.getPiecePosition() && otherPiece.getPieceType() == pieceType
                && pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }


    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31*result + piecePosition;
        result = 31*result + (isFirstMove ? 1 : 0);
        return result;
    }


    public  boolean isFirstMove() {
        return isFirstMove;
    }

    public int  getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public  int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType {

        BISHOP("B", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        KING("K", 10000){
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        KNIGHT("N", 300){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        PAWN("P", 100){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return true;
            }
        },
        QUEEN("Q", 900){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        },
        ROOK("R", 500){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }

            @Override
            public boolean isPawn() {
                return false;
            }
        };
        private String pieceName;
        private int value;


        @Override
        public String toString() {
            return this.pieceName;
        }

        PieceType(final String pieceName, final int value) {
            this.pieceName = pieceName;
            this.value = value;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();

        public  int getPieceValue() {
            return this.value;
        }

        public abstract boolean isPawn();
    }

}
