package com.chess.engine.player.ai;

import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PawnAnalyzer {

    private static final PawnAnalyzer INSTANCE = new PawnAnalyzer();

    public static final int ISOLATED_PAWN_PENALTY = -25;
    public static final int DOUBLED_PAWN_PENALTY = -25;

    private PawnAnalyzer() {
    }

    public static PawnAnalyzer get() {
        return INSTANCE;
    }

    public int isolatedPawnPenalty(final Player player) {
        return calculateIsolatedPawnPenalty(createPawnColumnTable(calculatePlayerPawns(player)));
    }

    public int doubledPawnPenalty(final Player player) {
        return calculatePawnColumnStack(createPawnColumnTable(calculatePlayerPawns(player)));
    }

    public int pawnStructureScore(final Player player) {
        final int[] pawnsOnColumnTable = createPawnColumnTable(calculatePlayerPawns(player));
        return calculatePawnColumnStack(pawnsOnColumnTable) + calculateIsolatedPawnPenalty(pawnsOnColumnTable) +
                calculatePawnPositionBonus(calculatePlayerPawns(player));
    }

    private static Collection<Piece> calculatePlayerPawns(final Player player) {
        final List<Piece> playerPawnLocations = new ArrayList<>(8);
        for(final Piece piece : player.getActivePieces()) {
            if(piece.getPieceType().isPawn()) {
                playerPawnLocations.add(piece);
            }
        }
        return ImmutableList.copyOf(playerPawnLocations);
    }

    private static int calculatePawnColumnStack(final int[] pawnsOnColumnTable) {
        int pawnStackPenalty = 0;
        for(final int pawnStack : pawnsOnColumnTable) {
            if(pawnStack > 1) {
                pawnStackPenalty += pawnStack;
            }
        }
        return pawnStackPenalty * DOUBLED_PAWN_PENALTY;
    }

    private static int calculateIsolatedPawnPenalty(final int[] pawnsOnColumnTable) {
        int numIsolatedPawns = 0;
        if(pawnsOnColumnTable[0] > 0 && pawnsOnColumnTable[1] == 0) {
            numIsolatedPawns += pawnsOnColumnTable[0];
        }
        if(pawnsOnColumnTable[7] > 0 && pawnsOnColumnTable[6] == 0) {
            numIsolatedPawns += pawnsOnColumnTable[7];
        }
        for(int i = 1; i < pawnsOnColumnTable.length - 1; i++) {
            if((pawnsOnColumnTable[i-1] == 0 && pawnsOnColumnTable[i+1] == 0)) {
                numIsolatedPawns += pawnsOnColumnTable[i];
            }
        }
        return numIsolatedPawns * ISOLATED_PAWN_PENALTY;
    }

    private static int[] createPawnColumnTable(final Collection<Piece> playerPawns) {
        final int[] table = new int[8];
        for(final Piece playerPawn : playerPawns) {
            table[playerPawn.getPiecePosition() % 8]++;
        }
        return table;
    }

    private static int calculatePawnPositionBonus(final Collection<Piece> playerPawns) {
        int pawnBonus = 0;
        for(final Piece playerPawn : playerPawns) {
            pawnBonus += getPawnBonus(playerPawn);
        }
        return pawnBonus;
    }

    private static int getPawnBonus(Piece pawn) {
        int pawnPosition = pawn.getPiecePosition();

        if (18 >= pawnPosition && pawnPosition >= 16 ) {
            return 3;
        }
        else if (21 >= pawnPosition && pawnPosition > 18 ) {
            return 6;
        } else if (23 >= pawnPosition && pawnPosition > 21 ) {
            return 3;
        }

        else if (26 <= pawnPosition && pawnPosition >  24) {
            return 8;
        } else if (29 >= pawnPosition && pawnPosition >  26) {
            return 12;
        } else if (31 >= pawnPosition && pawnPosition >  29) {
            return 8;
        }
        else if (34 <= pawnPosition && pawnPosition >  32) {
            return 8;
        } else if (37 >= pawnPosition && pawnPosition > 34) {
            return 12;
        } else if (39 >= pawnPosition && pawnPosition >  37) {
            return 8;

        } else if (42 <= pawnPosition && pawnPosition >  40) {
            return 3;
        } else if (45 >= pawnPosition && pawnPosition > 42) {
            return 6;
        } else if (47 >= pawnPosition && pawnPosition >  45) {
            return 3;
        }
        return 0;
    }
}
