
package com.chess.tests;

import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.*;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class BoardTest {

    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());

    }

}