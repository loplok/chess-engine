package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;


public class King extends Piece{

    private boolean isCastled;
    private final boolean canKingSideCastle;
    private final boolean canQueenSideCastle;
    private boolean isInCheck;
    private final static int[] CANDIDATE_MOVE_COORDINATE= {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int piecePosition, Alliance pieceAlliance, boolean isInCheck) {
        super(piecePosition, pieceAlliance, PieceType.KING, true, false);
        this.isCastled = false;
        this.canKingSideCastle = false;
        this.canQueenSideCastle = false;
        this.isInCheck = isInCheck;
    }

    public void setCastled(boolean castled) {
        this.isCastled = castled;
    }

    public King(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.KING, isFirstMove, false);
        this.isCastled = false;
        this.canKingSideCastle = false;
        this.canQueenSideCastle = false;
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    @Override
    public King movePiece(Move move) {
        return new King( move.getDestinationCoordinate(), move.getPieceThatMoved().getPieceAlliance(), false);
    }

    @Override
    public Collection<Move> calculateLegalMove(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
             final int candidateDestination = this.piecePosition + currentCandidate;

             if(isFirstColumnExclusion(this.piecePosition, currentCandidate) ||
                     isEightColumnExclusion(this.piecePosition, currentCandidate)) {
                 continue;
             }

             if(BoardUtils.isValidTileCoordinate(candidateDestination)) {
                 final Tile candidateTile = board.getTile(candidateDestination);

                 if (!candidateTile.isTileOccupied()) {
                     legalMoves.add(new MajorPieceMove(board, this, candidateDestination));
                 } else {
                     final Piece pieceAtDestination = candidateTile.getPiece();
                     final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                     if (this.pieceAlliance != pieceAlliance) {
                         legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestination,
                                 pieceAtDestination));
                     }
                 }

             }
         }
        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }

    private static boolean   isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == 9 || candidateOffset == 1 ||
                candidateOffset == -7);
    }
}
