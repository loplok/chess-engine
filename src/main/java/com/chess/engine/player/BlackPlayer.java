package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.KingSideCastleMove;
import com.chess.engine.move.Move;
import com.chess.engine.move.QueenSideCastleMove;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board, Collection<Move> blackLegalMoves, Collection<Move> whiteLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        List<Piece> blackPiecesNotTaken = new ArrayList<>();
       for (Piece piece: playBoard.getBlackPieces()) {
           if (!piece.getIsTaken()) {
               blackPiecesNotTaken.add(piece);
           }
        }
       return blackPiecesNotTaken;
    }

    @Override
    public Collection<Piece> getTakenPieces() {
        List<Piece> takenPieces = new ArrayList<>();
        for (Piece piece: playBoard.getBlackPieces()) {
            if(piece.getIsTaken()) {
                takenPieces.add(piece);
            }
        }
        return takenPieces;
    }

    @Override
    public Collection<Move> getMovesFromTaken() {
        final List<Move> legalMovesFromTaken = new ArrayList<>();
        for (Piece piece: getTakenPieces()) {
                if (piece.getIsTaken()) {
                    legalMovesFromTaken.addAll(piece.calculateLegalMove(this.playBoard));
                }
        }
        return legalMovesFromTaken;
    }

    @Override
    public void addPiece(Piece piece) {
        playBoard.addBlackPiece(piece);
    }

    @Override
    public void removePiece(Piece piece) {
        playBoard.blackPlayer().removePiece(piece);
    }


    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.playBoard.whitePlayer();
    }

    @Override
    public String toString() {
        return Alliance.BLACK.toString();
    }

    @Override
    protected Collection<Move> calculateKingCastle(final Collection<Move> moves,final Collection<Move> opponentsMoves) {


        if (this.isInCheck() || this.isCastled()) {
            return ImmutableList.copyOf(new ArrayList<>());
        }

        final List<Move> kingCastles = new ArrayList<>();

        if (this.getPlayerKing().isFirstMove() && this.getPlayerKing().getPiecePosition() == 4 && !this.isInCheck()) {
            // white kingside
            if (this.playBoard.getPiece(5) == null && this.playBoard.getPiece(6) == null) {
                final Piece kingSideRook = this.playBoard.getPiece(7);
                if (kingSideRook != null && kingSideRook.isFirstMove() &&
                        Player.calculateAttacks(5, opponentsMoves).isEmpty() &&
                        Player.calculateAttacks(6, opponentsMoves).isEmpty() &&
                        kingSideRook.getPieceType().isRook()) {
                    if (!BoardUtils.isKingPawnTrap(this.playBoard, this.getPlayerKing(), 12)) {
                        kingCastles.add(
                                new KingSideCastleMove(this.playBoard, this.getPlayerKing(),
                                        6, (Rook) kingSideRook, kingSideRook.getPiecePosition(),
                                        5));

                    }
                }
            }
            // white queen side
            if (this.playBoard.getPiece(1) == null && this.playBoard.getPiece(2) == null &&
                    this.playBoard.getPiece(3) == null) {
                final Piece queenSideRook = this.playBoard.getPiece(0);
                if (queenSideRook != null && queenSideRook.isFirstMove() &&
                        Player.calculateAttacks(2, opponentsMoves).isEmpty() &&
                        Player.calculateAttacks(3, opponentsMoves).isEmpty() &&
                        queenSideRook.getPieceType().isRook()) {
                    if (!BoardUtils.isKingPawnTrap(this.playBoard, this.getPlayerKing(), 12)) {
                        kingCastles.add(
                                new QueenSideCastleMove(this.playBoard,
                                        this.getPlayerKing(), 2, (Rook) queenSideRook,
                                        queenSideRook.getPiecePosition(), 3));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}


