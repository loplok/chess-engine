package com.chess.gui;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.*;


public class Table extends Observable {
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private  Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private final GameHistoryPanel gameHistory;
    private final PiecesPanel piecesPanel;
    private final MoveLog log;
    private final GameSetup setup;


    private BoardDirection direction= BoardDirection.NORMAL;

    private boolean highlightLegalMoves;

    private static final Dimension TILE_PANEL_SIZE = new Dimension(10,10);
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final static Dimension OUTER_FRAME = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_SIZE = new Dimension(400,350);

    private static final Table instance = new Table();
    private Move computerMove;

    public static Table get() {
        return instance;
    }

    public Table() {
        this.gameFrame = new JFrame("BugHouse");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistory = new GameHistoryPanel();
        this.piecesPanel = new PiecesPanel();
        this.boardPanel = new BoardPanel();
        this.log = new MoveLog();
        this.addObserver(new TableGameObserver());
        this.setup = new GameSetup(this.gameFrame, true);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.highlightLegalMoves = true;
        this.gameFrame.add(this.piecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistory, BorderLayout.EAST);
        this.gameFrame.setSize(OUTER_FRAME);
        this.gameFrame.setVisible(true);

    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistory().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferenceMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load from PGN file format");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("open pgn");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exit);
        return fileMenu;
    }

    private JMenu createPreferenceMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");

        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                direction = direction.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlighterCheckBox = new JCheckBoxMenuItem("Highlight all legal moves", false);

        legalMoveHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                highlightLegalMoves = legalMoveHighlighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckBox);
        return preferencesMenu;
    }


    private JMenu createOptionsMenu() {
        final JMenu options = new JMenu("JMenu");
        final JMenuItem setupGame = new JMenuItem("Setup Game");
        setupGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Table.get().getGameSetup().promptUser();
                Table.get().get().setUpUpdate(Table.get().getGameSetup());
            }
        });
        options.add(setupGame);

        return options;
    }

    private void setUpUpdate(GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private PiecesPanel getPiecesPanel() {
        return this.piecesPanel;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private static class TableGameObserver implements Observer {

        @Override
        public void update(Observable observable, Object o) {
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer())
                && !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()
                    && Table.get().getGameBoard().getCurrentPlayer().isStalemate()) {

                final ThinkTank tank = new ThinkTank();
                tank.execute();
            }

            if(Table.get().getGameBoard().currentPlayer().isInCheckMate()) {
                System.out.println("Game over!" + Table.get().getGameBoard().currentPlayer() + " loses");
            }

            if(Table.get().getGameBoard().currentPlayer().isStalemate()) {
                System.out.println("Game over!" + Table.get().getGameBoard().currentPlayer() + " loses");
            }
        }
    }

    private static class ThinkTank extends SwingWorker<Move, String> {

        private ThinkTank() {

        }

        @Override
        protected Move doInBackground() throws Exception {
            MiniMax mini = new MiniMax(4);
            final Move best = mini.execute(Table.get().getGameBoard(), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
            return best;
        }

        @Override
        public void done() {
            try {
                final Move best = get();

                Table.get().updateComputer(best);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(best).getTransitionBoardBoard());
                Table.get().getMoveLog().addMove(best);
                Table.get().getGameHistory().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateComputer(Move best) {
        this.computerMove = best;
    }

    private MoveLog getMoveLog() {
        return this.log;
    }

    private GameHistoryPanel getGameHistory() {
        return this.gameHistory;
    }

    private void updateGameBoard(Board transitionBoardBoard) {
        this.chessBoard = transitionBoardBoard;
    }

    private GameSetup getGameSetup() {
        return this.setup;
    }


    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();

            for(int i= 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_SIZE);
            validate();
        }

        public void drawBoard(final Board chessBoard) {
            removeAll();
            for(final TilePanel tilePanel : direction.traverse(boardTiles)) {
                tilePanel.drawTile(chessBoard);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }



    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }


    }


    private class TilePanel extends JPanel {

        private  int tileID;
        
        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_SIZE);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if(isRightMouseButton(mouseEvent)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                        } else if (isLeftMouseButton(mouseEvent)) {
                        if(sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            System.out.println(destinationTile);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileNumber(),
                                    destinationTile.getTileNumber());
                            final MoveTransition trans = chessBoard.currentPlayer().makeMove(move);
                            if(trans.getMoveStatus().isDone()) {
                                chessBoard = trans.getTransitionBoardBoard();
                                log.addMove(move);
                                System.out.println("done");
                            }
                            sourceTile = null;
                            humanMovedPiece = null;
                            destinationTile = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                gameHistory.redo(chessBoard, log);
                                piecesPanel.redo(log);
                                if(getGameSetup().isAIPlayer(chessBoard.currentPlayer())) {
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }

                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });

            validate();
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        private void highlightLegalMoves(final Board board) {
            if(highlightLegalMoves) {
                for(final  Move move: pieceLegalMoves(board)) {
                    if(move.getDestinationCoordinate() == this.tileID) {
                        try {
                            add(new JLabel((new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png"))))));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves (final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMove(board);
            }
            return Collections.emptyList();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if(board.getTile(this.tileID).isTileOccupied()) {
                 try {
                    String defaultPiecePath = "art/simple/";
                    final BufferedImage image = ImageIO.read(new File(defaultPiecePath
                            + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0,1)
                            + board.getTile(this.tileID).toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            boolean isLight = ((tileID + tileID/8) % 2 == 0);
            setBackground(isLight ? lightTileColor : darkTileColor);
            }
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }

}


