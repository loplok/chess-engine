package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

     protected final  Board board;
     protected final Piece pieceThatMoved;
     protected final int destinationOfPiece;
     protected final boolean isFirstMove;

     public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece pieceThatMoved, final int destinationOfPiece) {
        this.board = board;
        this.pieceThatMoved = pieceThatMoved;
        this.destinationOfPiece = destinationOfPiece;
        this.isFirstMove = pieceThatMoved.isFirstMove();
    }

    private Move(final Board board, final int destinationOfPiece) {
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
        final Board.Builder builder = new Board.Builder();

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


    public static final class MajorAttackMove extends AttackingMove {

        public MajorAttackMove(final Board board,final Piece pieceThatMoved,final int destinationOfPiece,final Piece attackedPiece) {
            super(board, pieceThatMoved, destinationOfPiece, attackedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return pieceThatMoved.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
        }

    }


    public static final class  MajorPieceMove extends Move {

        public MajorPieceMove(Board board, Piece pieceThatMoved, int destinationOfPiece) {
            super(board, pieceThatMoved, destinationOfPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorPieceMove && super.equals(other);
        }

        @Override
        public String toString() {
            return pieceThatMoved.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }

    public static class  AttackingMove extends Move {

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

    public static final class  PawnMove extends Move {

        public PawnMove(Board board, Piece pieceThatMoved, int destinationOfPiece) {
            super(board, pieceThatMoved, destinationOfPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
        }
    }

    public static  class  PawnAttackMove extends AttackingMove {

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

    public static final class  PawnEnPassantAttackMove extends PawnAttackMove {

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
            final Board.Builder builder = new Board.Builder();

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

    public static final class  PawnJump extends Move {

        public PawnJump(Board board, Piece pieceThatMoved, int destinationOfPiece) {
            super(board, pieceThatMoved, destinationOfPiece);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationOfPiece);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();

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

    }

    public static class PawnPromotion extends Move {

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
            final Board.Builder builder = new Board.Builder();
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

     static abstract class  CastleMove extends Move {
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
             final Board.Builder builder= new Board.Builder();
             for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
                 if (!this.pieceThatMoved.equals(piece) && this.pieceThatMoved.equals(castleRook)) {
                     builder.setPiece(piece);
                 }
             }
             for (final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
                 builder.setPiece(piece);
             }
             builder.setPiece(this.pieceThatMoved.movePiece(this));
             builder.setPiece(new Rook(this.castleRookHeadingTo,this.castleRook.getPieceAlliance()));
             builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
             board.currentPlayer().getPlayerKing().setCastled(true);
             System.out.println("CASTLING");
             return builder.build();
         }
     }

    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(Board board, Piece pieceThatMoved, int destinationOfPiece,
                                  final Rook castleRook, final int rookPosition, final int castleRookHeadingTo) {
            super(board, pieceThatMoved, destinationOfPiece, castleRook, rookPosition, castleRookHeadingTo);
        }

        @Override
        public String toString() {
            return "0-0";
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

    }

    public static final class  QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(Board board, Piece pieceThatMoved, int destinationOfPiece,
                                    final Rook castleRook, final int rookPosition, final int castleRookHeadingTo) {
            super(board, pieceThatMoved, destinationOfPiece, castleRook, rookPosition, castleRookHeadingTo);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }


        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static class InsertMove extends Move {
        public InsertMove(Board board, Piece piece, int piecePos) {
            super(board, piece, piecePos);
        }

        @Override
        public  Board execute() {
            final Board.Builder builder = new Board.Builder();


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

    static class  NullMove extends Move {

        private NullMove() {
            super(null, -65);
        }


        // should never reach here, just safety
        @Override
        public Board execute() {
            throw new RuntimeException("Not possible");
        }

        // makes clicking the same square during Move decision click and not throw NullExp;
        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Can't instantiate");
        }

        public static Move createMove(final Board board, final int currentCoordinate,
                                      final int destinateCoordinate) {
            for (final Move move: board.getAllMoves()) {
                if(move.getDestinationCoordinate() == destinateCoordinate) {
                    return move;
                }
            }

            return NULL_MOVE;
        }
    }
}
