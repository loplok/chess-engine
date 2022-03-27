package com.chess;

import com.chess.engine.move.Move;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.MoveFactory;
import com.chess.engine.player.MoveTransition;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;
import static junit.framework.Assert.assertTrue;


public class TestBugHouseInsertMove {

    @Test
    public void insertPawn() {
        final Board board = Board.createStandardBoard();
        final Board secondBoard = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer().makeMove(
                MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4")));

        Assert.assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition  t2 = t1.getTransitionBoard().currentPlayer().makeMove(
                MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5")));


        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f4")));
        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t4.getMoveStatus().isDone());

        secondBoard.addWhitePiece(t4.getMove().getAttackedPiece());

        Collection<Move> moves = secondBoard.getCurrentPlayer().getLegalMoves();
    }
}
