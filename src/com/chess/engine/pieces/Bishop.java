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


public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR = {-9, -7, 7, 9};

    public Bishop(int piecePosition, Alliance pieceAlliance) {
        super( piecePosition, pieceAlliance, PieceType.BISHOP, true);
    }

    public Bishop(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.BISHOP, isFirstMove);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop( move.getDestinationCoordinate(), move.getPieceThatMoved().getPieceAlliance());
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }

    @Override
    public Collection<Move> calculateLegalMove(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR) {
            int candidateDestination = this.piecePosition;

            while(BoardUtils.isValidTileCoordinate(candidateDestination)) {
                // break out if exclusion, same as with Knight when the evaluation does not work

                if(isFirstColumnExclusion(candidateDestination, candidateCoordinateOffset) ||
                    isEightColumnExclusion(candidateDestination, candidateCoordinateOffset)) {
                    break;
                }

                candidateDestination += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestination)) {
                    final Tile candidateTile = board.getTile(candidateDestination);

                    if (!candidateTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorPieceMove(board, this, candidateDestination));
                    } else {
                        final Piece pieceAtDestination = candidateTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackingMove(board, this, candidateDestination,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);

    }


}
