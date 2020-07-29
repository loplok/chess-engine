package com.chess.engine.player.ai;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Iterables;

import java.util.*;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int depth;
    private int quiescenceCount;
    private static final int QUIESCENCE = 10000;
    private int numBoardsEvaluated = 0;
    private int cutOffs = 0;
    private TimeEvaluator timerWhite;
    private TimeEvaluator timerBlack;
    private int secondsTotal;
    private int movesPlayed = 0;

    public MiniMax(final int depth, final int secondsTotal) {
        this.boardEvaluator =new StandardBoardEvaluator();
        this.depth = depth;
        this.quiescenceCount = 0;
        this.secondsTotal = secondsTotal;
    }

    public MiniMax(final int depth, final int secondsTotal,
                   final TimeEvaluator timerWhite,
                   final TimeEvaluator timerBlack) {
        this.boardEvaluator =new StandardBoardEvaluator();
        this.depth = depth;
        this.quiescenceCount = 0;
        this.secondsTotal = secondsTotal;
        this.timerBlack = timerBlack;
        this.timerWhite = timerWhite;
    }


    @Override
    public String toString() {
        return "MiniMax";
    }



    public BoardEvaluator getBoardEvaluator() {
        return this.boardEvaluator;
    }


    // TODO remake execute, take into consideration the taken pieces, when insert move, insert them into second board;
    @Override
    public Move execute(Board board) {

        int currentDepth = 1;

        int current;
        Move bestMove = null;

        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;

        HashMap<Integer, Move> map = new HashMap<>();

        double timePerMove = 0;
        Alliance allianceOnTurn;
        if (board.getCurrentPlayer().getAlliance().isWhite()) {
            timePerMove = timerWhite.openingTime(movesPlayed);
            allianceOnTurn = Alliance.WHITE;
        } else {
            timePerMove = timerBlack.openingTime(movesPlayed);
            allianceOnTurn = Alliance.BLACK;
        }

        long finish = (long) (System.currentTimeMillis() + timePerMove);


        while (System.currentTimeMillis() < finish) {
            int sizeOfMoves = board.getCurrentPlayer().getLegalMoves().size();
            Collection<Move> allMoves = new ArrayList<>(board.getCurrentPlayer().getLegalMoves());
            allMoves.addAll(board.calculateFromTaken(board.getCurrentPlayer().getTakenPieces()));

            for (Move move : allMoves) {
                final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus().isDone()) {
                    current = board.getCurrentPlayer().getAlliance().isWhite() ?
                            min(moveTransition.getTransitionBoard(), currentDepth - 1, highest, lowest) :
                            max(moveTransition.getTransitionBoard(), currentDepth - 1 , highest, lowest);
                    if (board.getCurrentPlayer().getAlliance().isWhite() && current > highest) {
                        highest = current;
                        bestMove = move;
                        if (moveTransition.getTransitionBoard().blackPlayer().isInCheckMate()) {
                            break;
                        }
                    } else if (board.getCurrentPlayer().getAlliance().isBlack() && current < lowest) {
                        lowest = current;
                        bestMove = move;
                        if (moveTransition.getTransitionBoard().whitePlayer().isInCheckMate()) {
                            break;
                        }
                    }
                }
            }
            currentDepth++;
        }

        System.out.println("Evaluated " + this.numBoardsEvaluated + " boards at depth " + currentDepth +
                " and found best move to be " + bestMove.toString() + " with " + this.cutOffs + " cutoffs produced");

        if (allianceOnTurn == Alliance.WHITE) {
            timerWhite.tick(timePerMove);
        } else {
            timerBlack.tick(timePerMove);
        }
        this.movesPlayed++;

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
                    this.cutOffs++;
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
                    this.cutOffs++;
                    return lowest;
                }
            }
        }
        return currentHighest;
    }
}
