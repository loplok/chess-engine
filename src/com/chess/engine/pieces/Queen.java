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

public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, true, false);
    }

    public Queen(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove,
                 final boolean isTaken) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, isFirstMove, isTaken);
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen( move.getDestinationCoordinate(), move.getPieceThatMoved().getPieceAlliance());
    }

    @Override
    public Collection<Move> calculateLegalMove(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR) {
            int candidateDestination = this.piecePosition;

            while(BoardUtils.isValidTileCoordinate(candidateDestination)) {
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
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestination,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);

    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 ||candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

}
