package models;

import models.enums.Color;
import models.enums.PieceType;
import models.pieces.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Objects;

public class Board {
    public static final String TITLE_ART =
            "##   ##    ##      ####     ####     ####    ##  ##   ######    ####     ####\n" +
            " ### ###   ####    ##  ##     ##     ##  ##   ##  ##   ##       ##  ##   ##  ##\n" +
            " #######  ##  ##   ##         ##     ##       ##  ##   ##       ##       ##\n" +
            " ## # ##  ######   ## ###     ##     ##       ######   ####      ####     ####\n" +
            " ##   ##  ##  ##   ##  ##     ##     ##       ##  ##   ##           ##       ##\n" +
            " ##   ##  ##  ##   ##  ##     ##     ##  ##   ##  ##   ##       ##  ##   ##  ##\n" +
            " ##   ##  ##  ##    ####     ####     ####    ##  ##   ######    ####     ####\n";

    private static Piece[][] GRID = new Piece[8][8];

    private static Map<Integer, Integer> frozenColumnTurns = new HashMap<>();
    private static Map<Player, Integer> imperialBarrierTurns = new HashMap<>(); 

    public Board() {
        initializePieces(Color.WHITE);
        initializePieces(Color.BLACK);
    }

    public static void initializePieces(Color color) {
        int row = (color == Color.WHITE) ? 7 : 0;

        GRID[row][0] = new Tower(row, 0, color, PieceType.TOWER);
        GRID[row][7] = new Tower(row, 7, color, PieceType.TOWER);
        GRID[row][1] = new Knight(row, 1, color, PieceType.KNIGHT);
        GRID[row][6] = new Knight(row, 6, color, PieceType.KNIGHT);
        GRID[row][2] = new Bisp(row, 2, color, PieceType.BISP);
        GRID[row][5] = new Bisp(row, 5, color, PieceType.BISP);
        GRID[row][3] = new Queen(row, 3, color, PieceType.QUEEN);
        GRID[row][4] = new King(row, 4, color, PieceType.KING);

        row = (color == Color.WHITE) ? 6 : 1;
        for (int i = 0; i < 8; i++) {
            GRID[row][i] = new Pawn(row, i, color, PieceType.PAWN);
        }
    }

    public static void updateBoard() {
    
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                GRID[x][y] = null;
            }
        }

        Player whitePlayer = GameLogic.getWhitePlayer();
        Player blackPlayer = GameLogic.getBlackPlayer();

        if (whitePlayer != null && whitePlayer.getPieces() != null) {
            for (Piece p : whitePlayer.getPieces()) {
                if (p != null && GameLogic.isValidBoardPosition(p.getPosX(), p.getPosY())) {
                   GRID[p.getPosX()][p.getPosY()] = p;
                }
            }
        }

        if (blackPlayer != null && blackPlayer.getPieces() != null) {
            for (Piece p : blackPlayer.getPieces()) {
                 if (p != null && GameLogic.isValidBoardPosition(p.getPosX(), p.getPosY())) {
                    GRID[p.getPosX()][p.getPosY()] = p;
                }
            }
        }
    }

    public static Piece getPieceFromBoard(int posX, int posY) {
        if (!GameLogic.isValidBoardPosition(posX, posY)) return null;
        return GRID[posX][posY];
    }
    
    public static void printBoard() {
        System.out.println("\n    a  b  c  d  e  f  g  h");
        System.out.println("   -------------------------");

        for (int x = 0; x < 8; x++) {
            System.out.print((8 - x) + " |");

            for (int y = 0; y < 8; y++) {
                Piece p = GRID[x][y];
                System.out.print(p == null ? " . " : " " + p.toString() + " ");
            }
            
            System.out.println("| " + (8 - x));
        }
        
        System.out.println("   -------------------------");
        System.out.println("    a  b  c  d  e  f  g  h");
    }
    
    public static Piece getPiece(int x, int y) {
        return getPieceFromBoard(x, y);
    }

    public static void setPiece(int x, int y, Piece piece) {
        if (GameLogic.isValidBoardPosition(x,y)) {
            GRID[x][y] = piece;
        }
    }

    // --- Métodos para Cartas ---
    public static void activateIceColumn(int columnIndexY, int durationInFullTurns) {
        frozenColumnTurns.put(columnIndexY, durationInFullTurns * 2);
        GameLogic.displayMessage("Coluna '" + GameLogic.coordsToAlgebraic(0, columnIndexY).charAt(0) + "' congelada por " + durationInFullTurns + " turno(s)!");
    }

    public static boolean isColumnFrozen(int columnIndexY) {
        return frozenColumnTurns.getOrDefault(columnIndexY, 0) > 0;
    }

    public static void activateImperialBarrier(Player player, int durationInOpponentTurns) {
        Objects.requireNonNull(player, "Player para Barreira Imperial não pode ser nulo.");
        imperialBarrierTurns.put(player, durationInOpponentTurns * 2);
        GameLogic.displayMessage("Barreira Imperial ativada para " + player.getName() + "!");
    }

    public static boolean isImperialBarrierActiveForPlayer(Player player) {
        if (player == null) return false;
        return imperialBarrierTurns.getOrDefault(player, 0) > 0;
    }
    
    public static boolean isPositionEmpty(int r, int c) {
        return getPieceFromBoard(r, c) == null;
    }

    public static void decrementActiveBoardEffectsTimers() {
        Iterator<Map.Entry<Integer, Integer>> iceIter = frozenColumnTurns.entrySet().iterator();
        while (iceIter.hasNext()) {
            Map.Entry<Integer, Integer> entry = iceIter.next();
            entry.setValue(entry.getValue() - 1);
            if (entry.getValue() <= 0) {
                iceIter.remove();
                GameLogic.displayMessage("Coluna '" + GameLogic.coordsToAlgebraic(0, entry.getKey()).charAt(0) + "' descongelou.");
            }
        }

        Iterator<Map.Entry<Player, Integer>> barrierIter = imperialBarrierTurns.entrySet().iterator();
        while(barrierIter.hasNext()){
            Map.Entry<Player, Integer> entry = barrierIter.next();
            entry.setValue(entry.getValue() - 1);
            if(entry.getValue() <= 0){
                barrierIter.remove();
                GameLogic.displayMessage("Barreira Imperial desativada para " + entry.getKey().getName() + ".");
            }
        }
    }
  
}