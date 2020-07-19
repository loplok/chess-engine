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

public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVES_COOR = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, true, false);
    }

    public Knight(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove, boolean isTaken) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, isFirstMove, isTaken);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean   isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6
                || candidateOffset == 10 || candidateOffset == 17);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight( move.getDestinationCoordinate(), move.getPieceThatMoved().getPieceAlliance());
    }

    @Override
    public Collection<Move> calculateLegalMove(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        if(!isTaken) {
        for (final int currentCandidatesOffset : CANDIDATE_MOVES_COOR) {
            int candidateDestination = this.piecePosition + currentCandidatesOffset;

            if (BoardUtils.isValidTileCoordinate(candidateDestination)) {
                //knights have to be evaluated differently due to the basic "mechanic of evaluating" not working in
                // these cases
                if(isFirstColumnExclusion(this.piecePosition, currentCandidatesOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidatesOffset) ||
                            isSeventhColumnExclusion(this.piecePosition, currentCandidatesOffset) ||
                                isEightColumnExclusion(this.piecePosition, currentCandidatesOffset)) {
                    continue;
                }
                    final Tile candidateTile = board.getTile(candidateDestination);

                    if (!candidateTile.isTileOccupied()) {
                        legalMoves.add(new MajorPieceMove(board, this, candidateDestination));
                    } else {
                        final Piece pieceAtDestination = candidateTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestination,
                                    pieceAtDestination));
                        }
                    }
                }
            }
            return ImmutableList.copyOf(legalMoves);
        } else {
            return List.copyOf(getLegalMovesFromTaken(board));
        }
    }

}

