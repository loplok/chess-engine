package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.MajorAttackMove;
import com.chess.engine.move.MajorPieceMove;
import com.chess.engine.move.Move;
import com.chess.engine.tile.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVES_COOR = {-8, -1, 1, 8};


    public Rook(int pieceAlliance, Alliance piecePosition) {
        super(pieceAlliance, piecePosition, PieceType.ROOK, true, false);
    }

    public Rook(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove,
                final boolean isTaken) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, isFirstMove, isTaken);
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook( move.getDestinationCoordinate(), move.getPieceThatMoved().getPieceAlliance());
    }

    @Override
    public Collection<Move> calculateLegalMove(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        if (!isTaken) {
            for (final int currentCandidatesOffset : CANDIDATE_MOVES_COOR) {
                int candidateDestination = this.piecePosition + currentCandidatesOffset;

                if (BoardUtils.isValidTileCoordinate(candidateDestination)) {
                    //knights have to be evaluated differently due to the basic "mechanic of evaluating" not working in
                    // these cases
                    if (isFirstColumnExclusion(this.piecePosition, currentCandidatesOffset) ||
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
            return List.copyOf(legalMoves);
        }
        // if is not on board, calculate all possible
        else {
             return List.copyOf(getLegalMovesFromTaken(board));
        }

    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset ==  1);
    }




}
