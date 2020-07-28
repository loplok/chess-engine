package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        List<Piece> whitePiecesNotTaken = new ArrayList<>();
        for (Piece piece: playBoard.getWhitePieces()) {
            if (!piece.getIsTaken()) {
                whitePiecesNotTaken.add(piece);
            }
        }
        return whitePiecesNotTaken;
    }

    @Override
    public Collection<Piece> getTakenPieces() {
        List<Piece> takenPieces = new ArrayList<>();
        for (Piece piece: playBoard.getWhitePieces()) {
            if (piece.getIsTaken()) {
                takenPieces.add(piece);
            }
        }
        return takenPieces;
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.playBoard.blackPlayer();
    }

    @Override
    public String toString() {
        return "White player";
    }

    @Override
    protected Collection<Move> calculateKingCastle(final Collection<Move> moves,final Collection<Move> opponentsMoves) {

     if (this.isInCheck() || this.isCastled()) {
            return ImmutableList.copyOf(new ArrayList<Move>());
        }

        final List<Move> kingCastles = new ArrayList<>();

        if (this.getPlayerKing().isFirstMove() && this.getPlayerKing().getPiecePosition() == 60 && !this.isInCheck()) {
            //blacks king side castle
            if (this.playBoard.getPiece(61) == null && this.playBoard.getPiece(62) == null) {
                final Piece kingSideRook = this.playBoard.getPiece(63);
                if (kingSideRook != null && kingSideRook.isFirstMove() &&
                        Player.calculateAttacks(61, opponentsMoves).isEmpty() &&
                        Player.calculateAttacks(62, opponentsMoves).isEmpty() &&
                        kingSideRook.getPieceType().isRook()) {
                    if (!BoardUtils.isKingPawnTrap(this.playBoard, this.getPlayerKing(), 52)) {
                        kingCastles.add(
                                new Move.KingSideCastleMove(this.playBoard, this.getPlayerKing(),
                                        62, (Rook) kingSideRook, kingSideRook.getPiecePosition(),
                                        61));

                    }
                }
            }
            //blacks queen side castle
            if (this.playBoard.getPiece(59) == null && this.playBoard.getPiece(59) == null &&
                    this.playBoard.getPiece(57) == null) {
                final Piece queenSideRook = this.playBoard.getPiece(56);
                if (queenSideRook != null && queenSideRook.isFirstMove() &&
                        Player.calculateAttacks(2, opponentsMoves).isEmpty() &&
                        Player.calculateAttacks(3, opponentsMoves).isEmpty() &&
                        queenSideRook.getPieceType().isRook()) {
                    if (!BoardUtils.isKingPawnTrap(this.playBoard, this.getPlayerKing(), 12)) {
                        kingCastles.add(
                                new Move.QueenSideCastleMove(this.playBoard,
                                        this.getPlayerKing(), 2, (Rook) queenSideRook,
                                        queenSideRook.getPiecePosition(), 3));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }

}
