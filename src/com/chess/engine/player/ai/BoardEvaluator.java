package com.chess.engine.player.ai;

import com.chess.engine.board.Board;

public interface BoardEvaluator {
    double evaluate(Board board, int depth);
}
