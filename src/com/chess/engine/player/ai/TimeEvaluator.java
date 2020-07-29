package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.bughouse.BugHouseGame;

public class TimeEvaluator {
    double millisTotal;
    double millisLeft;
    Board board;
    MiniMax alphaBeta;


    public TimeEvaluator(Board board, long secondsTotal) {
        this.millisTotal = secondsTotal * 1000;
        this.millisLeft = secondsTotal * 1000;
        this.board = board;
    }

    public void tick(double secondsWasted) {
        this.millisLeft -= secondsWasted;
    }

    public double openingTime(int movesPlayed) {

        if (movesPlayed <= 10 || board.getCurrentPlayer().getActivePieces().size() >= 13) {
            return millisLeft / 30;
        } else {
            return middleGameTime(movesPlayed);
        }
    }

    private double middleGameTime(int movesPlayed) {
        if (movesPlayed > 10 ||
                (board.getCurrentPlayer().getActivePieces().size() <= 13
                && board.getCurrentPlayer().getActivePieces().size() > 5)
                || movesPlayed < 25) {
            return millisLeft / 45;
        } else {
            return endGameTime(movesPlayed);
        }
    }

    private double endGameTime(int movesPlayed) {
        if (shouldResign()) {
            return -1;
        }
        if (movesPlayed > 30 ||
                (board.getCurrentPlayer().getActivePieces().size() <= 5
                        && board.getCurrentPlayer().getActivePieces().size() >= 1)) {
            return millisLeft / 15;
        }
        return millisLeft / 20;
    }

    public boolean shouldResign() {
        if (alphaBeta.getBoardEvaluator().evaluate(board, 1) > 2000
                && board.getCurrentPlayer().getAlliance().isBlack()) {
            return true;
        } else if (alphaBeta.getBoardEvaluator().evaluate(board, 1) < -2000
                && board.getCurrentPlayer().getAlliance().isWhite()) {
            return true;
        }

        return false;
    }

}
