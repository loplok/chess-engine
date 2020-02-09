package com.chess.engine.player.ai;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
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

    public MiniMax(final int depth) {
        this.boardEvaluator =new StandardBoardEvaluator();
        this.depth = depth;
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

    @Override
    public Move execute(Board board, Collection<Piece> whitePiecesTaken, Collection<Piece> blackPiecesTaken) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        double highest = Integer.MIN_VALUE;
        double lowest = Integer.MAX_VALUE;
        double current;


        System.out.println(board.currentPlayer() + "thinking with depth " + this.depth);

        Collection<Move> takenPiecesMove = getTakenPiecesMove(board, whitePiecesTaken, blackPiecesTaken);

        for(Iterator it = Iterables.concat(board.currentPlayer().getLegalMoves(), takenPiecesMove).iterator(); it.hasNext();) {
            Move move = (Move) it.next();
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus().isDone()) {
                    if(takenPiecesMove.contains(moveTransition.getMove())) {
                        if (board.currentPlayer().getAlliance() == Alliance.WHITE) {
                            whitePiecesTaken.remove(move.getPieceThatMoved());
                        } else {
                            blackPiecesTaken.remove(move.getPieceThatMoved());
                        }
                        it.remove();
                    }
                    current = board.currentPlayer().getAlliance().isWhite() ?
                            min(moveTransition.getTransitionBoardBoard(), this.depth - 1, whitePiecesTaken,
                                    blackPiecesTaken, takenPiecesMove) :
                            max(moveTransition.getTransitionBoardBoard(), this.depth - 1, whitePiecesTaken,
                                    blackPiecesTaken, takenPiecesMove);
                    if (board.currentPlayer().getAlliance().isWhite() && current >= highest) {
                        highest = current;
                        bestMove = move;
                    } else if (board.currentPlayer().getAlliance().isBlack() && current <= lowest) {
                        lowest = current;
                        bestMove = move;
                    }
                }
            }

            final long exec = System.currentTimeMillis() - startTime;
            System.out.println("Thought for " + exec + " ms and evaluated position at " + boardEvaluator.evaluate(board, depth));
            return bestMove;
        }


    public double min(final Board board, final int depth, Collection<Piece> whitePiecesTaken,
                   Collection<Piece> blackPiecesTaken, Collection<Move> takenPiecesMove) {
        if(depth == 0 || isItEndOfGame(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        double lowest = Integer.MAX_VALUE;
        for(Iterator it = Iterables.concat(board.currentPlayer().getLegalMoves(), takenPiecesMove).iterator(); it.hasNext();) {
            Move move = (Move) it.next();
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                if(takenPiecesMove.contains(moveTransition.getMove())) {
                    if (board.currentPlayer().getAlliance() == Alliance.WHITE) {
                        whitePiecesTaken.remove(move.getPieceThatMoved());
                    } else {
                        blackPiecesTaken.remove(move.getPieceThatMoved());
                    }
                    it.remove();
                }

                final double current = max(moveTransition.getTransitionBoardBoard(), depth -1, whitePiecesTaken,
                        blackPiecesTaken, takenPiecesMove);
                if(current <= lowest) {
                    lowest = current;
                }
            }
        }

        return lowest;
    }

    private boolean isItEndOfGame(Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isStalemate();
    }

    public double max(final Board board, final int depth, Collection<Piece> whitePiecesTaken,
                   Collection<Piece> blackPiecesTaken, Collection<Move> takenPiecesMove) {
        if(depth == 0) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        double highest = Integer.MIN_VALUE;
        for(Iterator it = Iterables.concat(board.currentPlayer().getLegalMoves(), takenPiecesMove).iterator(); it.hasNext();) {
            Move move = (Move) it.next();
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                if(takenPiecesMove.contains(moveTransition.getMove())) {
                    if (board.currentPlayer().getAlliance() == Alliance.WHITE) {

                        whitePiecesTaken.remove(move.getPieceThatMoved());
                    } else {
                        blackPiecesTaken.remove(move.getPieceThatMoved());
                    }
                    it.remove();
                }

                final double current = min(moveTransition.getTransitionBoardBoard(), depth -1,
                        whitePiecesTaken, blackPiecesTaken, takenPiecesMove);
                if(current >= highest) {
                    highest = current;
                }
            }
        }

        return highest;
    }

}
