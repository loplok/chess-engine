package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {


    private static final int CHECK_SCORE = 35;
    private static final int CHECK_MATE_SCORE = 5000000;

    @Override
    public int evaluate(Board board, int depth) {
        return (scorePlayer(board.whitePlayer(), depth) - scorePlayer(board.blackPlayer(), depth));
    }


    private int scorePlayer(Player player, int depth) {
        return (pieceValue(player) + mobility(player) + check(player) + checkMate(player, depth) + castled(player)
                + pawnStructure(player));
    }

    private static int pawnStructure(final Player player) {
        return PawnAnalyzer.get().pawnStructureScore(player);
    }

    private int castled(Player player) {
        return player.isCastled() ? 500 : 0;
    }

    private int checkMate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? (CHECK_MATE_SCORE * depthBonus(depth)) : 0;
    }

    private int depthBonus(int depth) {
        return depth == 0 ? 1 : 100*depth;
    }

    private int check(Player player) {

        return player.getOpponent().isInCheck() ? CHECK_SCORE : 0;

    }



    private int mobility(Player player) {

        return player.getLegalMoves().size()*5;
    }

    private static int pieceValue(final Player player) {
        int pieceScore = 0;
        for(final Piece piece: player.getActivePieces()) {
            pieceScore+=piece.getPieceValue();
        }
        return pieceScore;
    }

}
