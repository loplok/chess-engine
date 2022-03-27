package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};


    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, true, false);
    }

    public Pawn(final Alliance pieceAlliance,
                final int piecePosition,
                final boolean isFirstMove, boolean isTaken) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove, isTaken);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getPieceThatMoved().getPieceAlliance());
    }

    public Piece getPromotionPiece() {
        return new Queen(this.pieceAlliance, this.piecePosition, false, false);
    }

    @Override
    public Collection<Move> calculateLegalMove(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        if (!isTaken) {
            for (final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {

                final int candidateDestination = this.piecePosition + this.getPieceAlliance().getDirection() * currentCandidate;

                if (!BoardUtils.isValidTileCoordinate(candidateDestination)) {
                    continue;
                }

                if (currentCandidate == 8 && !board.getTile(candidateDestination).isTileOccupied()) {
                    if (this.pieceAlliance.isPawnPromotionSquare(candidateDestination)) {
                        legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestination)));
                    } else {
                        legalMoves.add(new PawnMove(board, this, candidateDestination));
                    }
                } else if (currentCandidate == 16 && this.isFirstMove() &&
                        ((BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isBlack())
                                || (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                    final int tileBehindTheCandidate = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                    if (!board.getTile(tileBehindTheCandidate).isTileOccupied() &&
                            !board.getTile(candidateDestination).isTileOccupied()) {
                        legalMoves.add(new PawnJump(board, this, candidateDestination));
                    }
                }
                // when pawn attacks, it does so in a diagonal, but only one tile in front of him. this solves it, not clean,
                // will rewrite or make it clearer to the reader;
                else if (currentCandidate == 7 &&
                        !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                                (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                    if (board.getTile(candidateDestination).isTileOccupied()) {
                        final Piece pieceOnCandidate = board.getTile(candidateDestination).getPiece();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            if (this.pieceAlliance.isPawnPromotionSquare(candidateDestination)) {
                                legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
                                        candidateDestination, pieceOnCandidate)));
                            } else {
                                legalMoves.add(new PawnAttackMove(board, this, candidateDestination, pieceOnCandidate));
                            }
                        }
                    } else if (board.getEnPassantPawn() != null) {
                        if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition +
                                this.pieceAlliance.getOppositeDirection())) {
                            final Piece pieceOnCandidate = board.getEnPassantPawn();
                            if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                                legalMoves.add(new PawnEnPassantAttackMove(board, this,
                                        candidateDestination, pieceOnCandidate));
                            }
                        }
                    }
                } else if (currentCandidate == 9 &&
                        !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                                (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                    if (board.getTile(candidateDestination).isTileOccupied()) {
                        final Piece pieceOnCandidate = board.getTile(candidateDestination).getPiece();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            if (this.pieceAlliance.isPawnPromotionSquare(candidateDestination)) {
                                legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
                                        candidateDestination, pieceOnCandidate)));
                            } else {
                                legalMoves.add(new PawnAttackMove(board, this, candidateDestination,
                                        pieceOnCandidate));
                            }
                        }
                    } else if (board.getEnPassantPawn() != null) {
                        if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition -
                                this.pieceAlliance.getOppositeDirection())) {
                            final Piece pieceOnCandidate = board.getEnPassantPawn();
                            if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                                legalMoves.add(new PawnEnPassantAttackMove(board, this,
                                        candidateDestination, pieceOnCandidate));
                            }
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
