package com.chess.engine.player.ai;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int depth;
    private int quiescenceCount;
    private static final int QUIESCENCE = 10000;

    public MiniMax(final int depth) {
        this.boardEvaluator =new StandardBoardEvaluator();
        this.depth = depth;
        this.quiescenceCount = 0;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    public Collection<Move> getTakenPiecesMove(Board board,Collection<Piece> whitePiecesTaken, Collection<Piece> blackPiecesTaken ) {
        return (board.currentPlayer().getAlliance().isWhite()) ?
                board.currentPlayer().getLegalFromTaken(whitePiecesTaken) :
                board.currentPlayer().getLegalFromTaken(blackPiecesTaken);
    }

    // TODO remake execute, take into consideration the taken pieces, when insert move, insert them into second board;
    @Override
    public Move execute(Board board) {

        // final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int current;

        for (Move move: board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                current = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), this.depth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getTransitionBoard(), this.depth - 1, highestSeenValue, lowestSeenValue);
                if (board.getCurrentPlayer().getAlliance().isWhite() && current > highestSeenValue) {
                    highestSeenValue = current;
                    bestMove = move;
                    if(moveTransition.getTransitionBoard().blackPlayer().isInCheckMate()) {
                        break;
                    }
                }
                else if (board.getCurrentPlayer().getAlliance().isBlack() && current < lowestSeenValue) {
                    lowestSeenValue = current;
                    bestMove = move;
                    if(moveTransition.getTransitionBoard().whitePlayer().isInCheckMate()) {
                        break;
                    }
                }
            }
        }
        return bestMove;
    }


    private int min(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getTransitionBoard();
                currentLowest = Math.min(currentLowest, max(toBoard,
                        depth -1,  highest, currentLowest));
                if (currentLowest <= highest) {
                    return highest;
                }
            }
        }
        return currentLowest;
    }
    /*
    private int calculateQuiescenceDepth(final Board toBoard,
                                         final int depth) {
        if(depth == 1 && this.quiescenceCount < QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.currentPlayer().isInCheck()) {
                activityMeasure += 1;
            }
            for(final Move move: BoardUtils.lastNMoves(toBoard, 2)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                return 2;
            }
        }
        return depth - 1;
    }
    */

    private boolean isItEndOfGame(Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isStalemate();
    }

    private int max(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getTransitionBoard();
                currentHighest = Math.max(currentHighest, min(toBoard,
                       depth - 1, currentHighest, lowest));
                if (currentHighest >= lowest) {
                    return lowest;
                }
            }
        }
        return currentHighest;
    }
}
