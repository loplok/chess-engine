package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

public class MoveFactory {
    public static final Move NULL_MOVE = new NullMove();

    private MoveFactory() {
        throw new RuntimeException("Can't instantiate");
    }

    public static Move createMove(final Board board, final int currentCoordinate,
                                  final int destinateCoordinate) {
        for (final Move move: board.getAllMoves()) {
            if(move.getDestinationCoordinate() == destinateCoordinate &&
                    move.getCurrentCoordinate() == currentCoordinate) {
                return move;
            }
        }

        return NULL_MOVE;
    }

    public static Move createInsertMove(final Board board, final int destinationCoordinate,
                                        final Piece.PieceType pieceType) {
        for (final Move move: board.getAllMoves()) {
            if (move.getDestinationCoordinate() == destinationCoordinate && move.isInsertMove()
                    && pieceType.toString().equals(move.pieceThatMoved.toString())) {
                return move;
            }
        }
        return NULL_MOVE;
    }

    public static Move getNullMove() {
        return NULL_MOVE;
    }
}
