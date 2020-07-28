package com.chess.tests;


import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.*;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class TestBugHouseInsertMove {

    @Test
    public void insertPawn() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer().makeMove(
                Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4")));

        Assert.assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition  t2 = t1.getTransitionBoard().currentPlayer().makeMove(
                Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5")));

        List<Move> moves = (List<Move>) t2.getTransitionBoard().blackPlayer().getLegalMoves();
        //assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t3.getMoveStatus().isDone());

    }

}
