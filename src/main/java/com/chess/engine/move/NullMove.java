package com.chess.engine.move;

import com.chess.engine.board.Board;

public class NullMove extends Move {

    public NullMove() {
        super(null, -65);
    }


    // should never reach here, just safety
    @Override
    public Board execute() {
        throw new RuntimeException("Not possible");
    }

    // makes clicking the same square during Move decision click and not throw NullExp;
    @Override
    public int getCurrentCoordinate() {
        return -1;
    }
}
