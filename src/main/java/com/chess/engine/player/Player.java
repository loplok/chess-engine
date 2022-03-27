package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board playBoard;
    protected final King king;
    protected Collection<Move> legalMoves;
    private final boolean isInCheck;


    Player(final Board board, final Collection<Move> legalMoves,
           final Collection<Move> opponentsMoves) {
        this.playBoard = board;
        this.king = createKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastle(legalMoves,opponentsMoves),
                getMovesFromTaken()));
        this.isInCheck = !calculateAttacks(this.king.getPiecePosition(), opponentsMoves).isEmpty();
    }



    protected static Collection<Move> calculateAttacks(int piecePosition,
                                            Collection<Move> opponentMoves) {
        final List<Move> attackMove = new ArrayList<>();
        for(final Move move: opponentMoves) {
            if(piecePosition == move.getDestinationCoordinate()) {
                attackMove.add(move);
            }
        }
        return ImmutableList.copyOf(attackMove);
    }





    public King createKing() {
        for(final Piece piece: getActivePieces()) {
            if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board!");
    }

    public King getPlayerKing() {
        return this.king;
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Collection<Piece> getTakenPieces();

    public abstract Collection<Move> getMovesFromTaken();

    public abstract void addPiece(Piece piece);

    public abstract void removePiece(Piece piece);

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    private boolean isMoveLegal(Move m) {
        return this.legalMoves.contains(m);
    }


    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasMovesLeft();
    }

    private boolean hasMovesLeft(){
        for(final Move move: this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStalemate() {
        return !this.isInCheck && !hasMovesLeft();
    }

    public boolean isCastled() {
        return this.king.isCastled();
    }

    public MoveTransition makeMove(final Move move) {
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.playBoard,
                    move,  MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move>  kingAttacks = Player.calculateAttacks
                (transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                        transitionBoard.currentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.playBoard, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move,  MoveStatus.DONE);

    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    protected abstract Collection<Move> calculateKingCastle(Collection<Move> moves,Collection<Move> opponentsMoves);

}
