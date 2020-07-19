package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;

    private final Collection<Piece> whitePiecesOnBoard;
    private final Collection<Piece> blackPiecesOnBoard;



    private final Pawn enPassantPawn;


    private final Player currentPlayer;

    private final WhitePlayer white;
    private final BlackPlayer black;


    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePiecesOnBoard = calculateActive(this.gameBoard, Alliance.WHITE);
        this.blackPiecesOnBoard = calculateActive(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.EnPassantPawn;


        final Collection<Move> whiteLegalMoves = calculate(this.whitePiecesOnBoard);
        final Collection<Move> blackLegalMoves = calculate(this.blackPiecesOnBoard);

        this.white = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.black = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer(), this.blackPlayer());
    }

    public Piece getPiece(final int coordinate) {
        return gameBoard.get(coordinate).getPiece();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(int i =0; i < BoardUtils.NUM_TILES; i++) {
            final String tileT = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileT));
            if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player whitePlayer() {
        return this.white;
    }

    public Player blackPlayer() {
        return this.black;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPiecesOnBoard;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePiecesOnBoard;
    }

    public Collection<Tile> getUnoccupiedTiles() {
        Collection<Tile> unoccupiedTiles = new ArrayList<>();
        for (final Tile tile: gameBoard) {
            if (!tile.isTileOccupied()) {
                unoccupiedTiles.add(tile);
            }
        }
        return unoccupiedTiles;
    }

    public Collection<Piece> getAllPieces() {
        return (Collection<Piece>) Iterables.concat(this.whitePiecesOnBoard, this.blackPiecesOnBoard);
    }

    private Collection<Move> calculate(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece: pieces) {
            legalMoves.addAll(piece.calculateLegalMove(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActive(List<Tile> gameBoard, Alliance alliance) {
        final List<Piece> activeOnBoard = new ArrayList<>();

        for (final Tile tile: gameBoard) {
            if(tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance) {
                    activeOnBoard.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activeOnBoard);
    }


    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board secondBoard() {
        // because the engine tends to play similar games, what i did is just gave it another starting position
        // so it doesnt end up on the same position every time, this can be done by  for ex. making a move for black
        // and white and starting the game, and results in different game everytime
        final Builder builder = new Builder();

        builder.setPiece(new Rook(0,Alliance.BLACK));
        builder.setPiece(new Knight(1,Alliance.BLACK));
        builder.setPiece(new Bishop(2,Alliance.BLACK));
        builder.setPiece(new Queen(3,Alliance.BLACK));
        builder.setPiece(new King(4,Alliance.BLACK, false));
        builder.setPiece(new Bishop(5,Alliance.BLACK));
        builder.setPiece(new Knight(6,Alliance.BLACK));
        builder.setPiece(new Rook(7,Alliance.BLACK));
        builder.setPiece(new Pawn(8,Alliance.BLACK));
        builder.setPiece(new Pawn(9,Alliance.BLACK));
        builder.setPiece(new Pawn(18,Alliance.BLACK));
        builder.setPiece(new Pawn(11,Alliance.BLACK));
        builder.setPiece(new Pawn(12,Alliance.BLACK));
        builder.setPiece(new Pawn(13,Alliance.BLACK));
        builder.setPiece(new Pawn(14,Alliance.BLACK));
        builder.setPiece(new Pawn(15,Alliance.BLACK));

        builder.setPiece(new Rook(63, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(44, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }


    public static Board createStandardBoard() {
        final Builder builder = new Builder();

        builder.setPiece(new Rook(0,Alliance.BLACK));
        builder.setPiece(new Knight(1,Alliance.BLACK));
        builder.setPiece(new Bishop(2,Alliance.BLACK));
        builder.setPiece(new Queen(3,Alliance.BLACK));
        builder.setPiece(new King(4,Alliance.BLACK, false));
        builder.setPiece(new Bishop(5,Alliance.BLACK));
        builder.setPiece(new Knight(6,Alliance.BLACK));
        builder.setPiece(new Rook(7,Alliance.BLACK));
        builder.setPiece(new Pawn(8,Alliance.BLACK));
        builder.setPiece(new Pawn(9,Alliance.BLACK));
        builder.setPiece(new Pawn(10,Alliance.BLACK));
        builder.setPiece(new Pawn(11,Alliance.BLACK));
        builder.setPiece(new Pawn(12,Alliance.BLACK));
        builder.setPiece(new Pawn(13,Alliance.BLACK));
        builder.setPiece(new Pawn(14,Alliance.BLACK));
        builder.setPiece(new Pawn(15,Alliance.BLACK));

        builder.setPiece(new Rook(63, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public Player currentPlayer() {
        return currentPlayer;
    }

    public Iterable<Move> getAllMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.white.getLegalMoves(), this.black.getLegalMoves()));
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public static class Builder {

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
}
