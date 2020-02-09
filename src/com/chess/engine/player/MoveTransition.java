package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.concurrent.Future;

public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatus status) {
        this.move = move;
        this.transitionBoard = transitionBoard;
        this.moveStatus = status;
    }

    public Move getMove() {
        return this.move;
    }


    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
    public Board getTransitionBoardBoard() {
        return this.transitionBoard;
    }
}
