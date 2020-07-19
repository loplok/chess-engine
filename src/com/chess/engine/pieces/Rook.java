package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

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
                        legalMoves.add(new Move.MajorPieceMove(board, this, candidateDestination));
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
        }
        // if is not on board, calculate all possible
        else {
            List.copyOf(getLegalMovesFromTaken(board));
        }

        return List.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset ==  1);
    }

    @Override
    public Collection<Move> getLegalMovesFromTaken(Board board) throws UnsupportedOperationException{
        List<Move> legalMovesFromTaken = new ArrayList<>();
        if (isTaken) {
            for (Tile tile: board.getUnoccupiedTiles()) {
                legalMovesFromTaken.add(new Move.InsertMove(board, this, tile.getTileNumber()));
            }
            return legalMovesFromTaken;
        }
        else throw new UnsupportedOperationException("Should not get here, something went wrong");
    }


}
