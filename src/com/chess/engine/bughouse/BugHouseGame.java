package com.chess.engine.bughouse;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

public class BugHouseGame {

    private Board firstBoard;
    private Board secondBoard;


    public BugHouseGame(Board firstGame, Board secondGame, int depth) {
        this.firstBoard = firstGame;
        this.secondBoard = secondGame;
        playGame(depth);
    }





    private boolean isFirstFinished() {
        return firstBoard.getCurrentPlayer().isInCheckMate() || firstBoard.currentPlayer().isStalemate();
    }

    private boolean isSecondFinished() {
        return secondBoard.getCurrentPlayer().isInCheckMate() || secondBoard.currentPlayer().isStalemate();
    }


   // TODO REMAKE THIS WHOLE SECTION, HANDLE SENDING PIECES AFTER INSERT MOVE TO THE OTHER BOARD;

    private void playGame(int depth) {

        while (!isFirstFinished() && !isSecondFinished()) {
            final MoveStrategy strat = new MiniMax(depth);
            //find Moves for both games;
                final Move move = strat.execute(this.firstBoard);
                final Move moveOnSecondBoard = strat.execute(this.secondBoard);

            if (move.isAttacked()) {
                Piece piece = move.getAttackedPiece();
                Alliance alliance = move.getBoard().currentPlayer().getAlliance();
                if (alliance == Alliance.BLACK) {
                    secondBoard.addWhitePiece(piece);
                } else {
                    secondBoard.addBlackPiece(piece);
                }
            }

            if (moveOnSecondBoard.isAttacked()) {
                Piece pieceOnSecondBoard = moveOnSecondBoard.getAttackedPiece();
                if (secondBoard.currentPlayer().getAlliance() == Alliance.BLACK) {
                    firstBoard.addWhitePiece(pieceOnSecondBoard);
                } else {
                    firstBoard.addBlackPiece(pieceOnSecondBoard);
                }
            }

            //System.out.println(firstBoard.currentPlayer().toString() + "made move " + move.toString());
            // System.out.println(secondBoard.currentPlayer().toString() + "made move " + moveOnSecondBoard.toString());
            // execute both moves;
            MoveTransition trans = firstBoard.currentPlayer().makeMove(move);
            MoveTransition tranSecondBoard = secondBoard.currentPlayer().makeMove(moveOnSecondBoard);
            firstBoard = trans.getTransitionBoard();
            secondBoard = tranSecondBoard.getTransitionBoard();
            System.out.println(firstBoard);
            System.out.println(secondBoard);
        }
    }
 
}

