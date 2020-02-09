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
    private List<Piece> gameOneBlackPieces;
    private List<Piece> gameOneWhitePieces;
    private List<Piece> gameTwoBlackPieces;
    private List<Piece> gameTwoWhitePieces;

    public BugHouseGame(Board firstGame, Board secondGame, int depth) {
        this.firstBoard = firstGame;
        this.secondBoard = secondGame;
        this.gameOneBlackPieces = new ArrayList<>();
        this.gameOneWhitePieces = new ArrayList<>();
        this.gameTwoBlackPieces = new ArrayList<>();
        this.gameTwoWhitePieces = new ArrayList<>();

        playGame(depth);

    }

    public void printTaken() {
        for(Piece piece: gameOneBlackPieces) {
            System.out.print(piece.getPieceType().toString() + " ");
        }
        System.out.println();
        for(Piece piece: gameOneWhitePieces) {
            System.out.print(piece.getPieceType().toString() + " ");
        }
        System.out.println();
        for(Piece piece: gameTwoBlackPieces) {
            System.out.print(piece.getPieceType().toString() + " ");
        }
        System.out.println();
        for(Piece piece: gameTwoWhitePieces) {
            System.out.print(piece.getPieceType().toString() + " ");
        }
    }



    private boolean isFirstFinished() {
        return firstBoard.getCurrentPlayer().isInCheckMate() || firstBoard.currentPlayer().isStalemate();
    }

    private boolean isSecondFinished() {
        return secondBoard.getCurrentPlayer().isInCheckMate() || secondBoard.currentPlayer().isStalemate();
    }

    private void playGame(int depth) {

        while (!isFirstFinished() && !isSecondFinished()) {
            final MoveStrategy strat = new MiniMax(depth);
            //find Moves for both games;
                final Move move = strat.execute(this.firstBoard, this.gameTwoWhitePieces, this.gameTwoBlackPieces);
                final Move moveOnSecondBoard = strat.execute(this.secondBoard, this.gameOneWhitePieces, this.gameOneBlackPieces);

            // if is capture, add it to the second game for next usage;
            System.out.println(move.toString());
            if (move.isAttacked()) {
                Piece piece = move.getAttackedPiece();
                Alliance alliance = move.getBoard().currentPlayer().getAlliance();
                if (alliance == Alliance.BLACK) {
                    gameTwoWhitePieces.add(piece);
                } else {
                    gameTwoBlackPieces.add(piece);
                }
            }
            // same for the other game, if is capture, add the piece to partners availability;
            if (moveOnSecondBoard.isAttacked()) {
                Piece pieceOnSecondBoard = moveOnSecondBoard.getAttackedPiece();
                if (secondBoard.currentPlayer().getAlliance() == Alliance.BLACK) {
                    gameOneWhitePieces.add(pieceOnSecondBoard);
                } else {
                    gameOneBlackPieces.add(pieceOnSecondBoard);
                }
            }

            if(move.isInsertMove() || moveOnSecondBoard.isInsertMove()) {
                System.out.println("Insert move");
            }
            if(move.isAttacked() || moveOnSecondBoard.isAttacked()) {
                System.out.println("Taking a piece move");
            }

            System.out.println(firstBoard.currentPlayer().toString() + "made move " + move.toString());
            System.out.println(secondBoard.currentPlayer().toString() + "made move " + moveOnSecondBoard.toString());
            // execute both moves;
            MoveTransition trans = firstBoard.currentPlayer().makeMove(move);
            MoveTransition tranSecondBoard = secondBoard.currentPlayer().makeMove(moveOnSecondBoard);
            firstBoard = trans.getTransitionBoardBoard();
            secondBoard = tranSecondBoard.getTransitionBoardBoard();
            System.out.println(firstBoard);
            System.out.println(secondBoard);
        }
    }



}

