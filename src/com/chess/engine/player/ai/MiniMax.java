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
    private int numBoardsEvaluated = 0;

    public MiniMax(final int depth) {
        this.boardEvaluator =new StandardBoardEvaluator();
        this.depth = depth;
        this.quiescenceCount = 0;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }


    // TODO remake execute, take into consideration the taken pieces, when insert move, insert them into second board;
    @Override
    public Move execute(Board board) {

        // final long startTime = System.currentTimeMillis();
        int current;
        Move bestMove = null;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;


        for (Move move: board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                current = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), this.depth - 1, highest, lowest) :
                        max(moveTransition.getTransitionBoard(), this.depth - 1, highest, lowest);
                if (board.getCurrentPlayer().getAlliance().isWhite() && current > highest) {
                    highest = current;
                    bestMove = move;
                    if(moveTransition.getTransitionBoard().blackPlayer().isInCheckMate()) {
                        break;
                    }
                }
                else if (board.getCurrentPlayer().getAlliance().isBlack() && current < lowest) {
                    lowest = current;
                    bestMove = move;
                    if(moveTransition.getTransitionBoard().whitePlayer().isInCheckMate()) {
                        break;
                    }
                }
            }
        }
        System.out.println(this.numBoardsEvaluated);
        return bestMove;
    }


    private int min(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0) {
            this.numBoardsEvaluated++;
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
    private int quiescenceSearch(final Board toBoard,
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


    private int max(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0) {
            this.numBoardsEvaluated++;
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
