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
        playBoard.addWhitePiece(piece);
    }

    @Override
    public void removePiece(Piece piece) {
        playBoard.whitePlayer().removePiece(piece);
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
                                new KingSideCastleMove(this.playBoard, this.getPlayerKing(),
                                        62, (Rook) kingSideRook, kingSideRook.getPiecePosition(),
                                        61));

                    }
                }
            }
            //blacks queen side castle
            if(this.playBoard.getPiece(59) == null && this.playBoard.getPiece(58) == null &&
                    this.playBoard.getPiece(57) == null) {
                final Piece queenSideRook = this.playBoard.getPiece(56);
                if(queenSideRook != null && queenSideRook.isFirstMove()) {
                    if(Player.calculateAttacks(58, opponentsMoves).isEmpty() &&
                            Player.calculateAttacks(59, opponentsMoves).isEmpty() && queenSideRook.getPieceType().isRook()) {
                        if(!BoardUtils.isKingPawnTrap(this.playBoard, this.king, 52)) {
                            kingCastles.add(new QueenSideCastleMove(this.playBoard, this.king, 58, (Rook) queenSideRook, queenSideRook.getPiecePosition(), 59));
                        }
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }

}
