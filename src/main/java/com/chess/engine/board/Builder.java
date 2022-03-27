package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public class Builder {

    Move transitionMove;
    Alliance nextMoveMaker;
    Map<Integer, Piece> boardConfig;
    Pawn EnPassantPawn;

    public Builder() {
        this.boardConfig = new HashMap<>(32, 1.0f);
    }

    public Builder setPiece(final Piece piece) {
        this.boardConfig.put(piece.getPiecePosition(), piece);
        return this;
    }

    public Builder setMoveTransition(final Move transitionMove) {
        this.transitionMove = transitionMove;
        return this;
    }

    public Builder setMoveMaker(final Alliance alliance) {
        this.nextMoveMaker = alliance;
        return this;
    }

    public Board build() {
        return new Board(this);
    }

    public void setEnPassant(Pawn setEnPassantPawn) {
        this.EnPassantPawn = setEnPassantPawn;
    }
}
