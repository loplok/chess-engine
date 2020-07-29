package com.chess.tests;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MoveStrategy;
import com.chess.engine.player.ai.MiniMax;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestCastling {

    @Test
    public void testWhiteKingSideCastle() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer().makeMove(
                Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4")));

        Assert.assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(
                Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"), BoardUtils.getCoordinateAtPosition("e5")));

        List<Move> moves = (List<Move>) t2.getTransitionBoard().blackPlayer().getLegalMoves();
        //assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t4.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t5.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d6"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t6.getMoveStatus().isDone());
        final Move wm1 = Move.MoveFactory
                .createMove(t6.getTransitionBoard(), BoardUtils.getCoordinateAtPosition(
                        "e1"), BoardUtils.getCoordinateAtPosition("g1"));
        assertTrue(t6.getTransitionBoard().currentPlayer().getLegalMoves().contains(wm1));
        final MoveTransition t7 = t6.getTransitionBoard().currentPlayer().makeMove(wm1);
        assertTrue(t7.getMoveStatus().isDone());
        assertTrue(t7.getTransitionBoard().whitePlayer().isCastled());
    }

    @Test
    public void testWhiteQueenSideCastle() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t4.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("c1"),
                        BoardUtils.getCoordinateAtPosition("d2")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t5.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d6"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t6.getMoveStatus().isDone());
        final MoveTransition t7 = t6.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t6.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t7.getMoveStatus().isDone());
        final MoveTransition t8 = t7.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t7.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("h7"),
                        BoardUtils.getCoordinateAtPosition("h6")));
        assertTrue(t8.getMoveStatus().isDone());
        final MoveTransition t9 = t8.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t8.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t9.getMoveStatus().isDone());
        final MoveTransition t10 = t9.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t9.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("h6"),
                        BoardUtils.getCoordinateAtPosition("h5")));
        assertTrue(t10.getMoveStatus().isDone());
        final Move wm1 = Move.MoveFactory
                .createMove(t10.getTransitionBoard(), BoardUtils.getCoordinateAtPosition(
                        "e1"), BoardUtils.getCoordinateAtPosition("c1"));
        assertTrue(t10.getTransitionBoard().currentPlayer().getLegalMoves().contains(wm1));
        final MoveTransition t11 = t10.getTransitionBoard().currentPlayer().makeMove(wm1);
        assertTrue(t11.getMoveStatus().isDone());
    }
}
