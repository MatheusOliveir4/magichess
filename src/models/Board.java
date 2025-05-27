package models;
import models.enums.Color;
import models.enums.PieceType;
import models.pieces.Bisp;
import models.pieces.King;
import models.pieces.Knight;
import models.pieces.Pawn;
import models.pieces.Piece;
import models.pieces.Queen;
import models.pieces.Tower;

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
        System.out.println("Update board aqui");

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                GRID[x][y] = null;
            }
        }

        Player whitePlayer = GameLogic.getWhitePlayer();
        Player blackPlayer = GameLogic.getBlackPlayer();

        if (whitePlayer != null) {
            for (Piece p : whitePlayer.getPieces()) {
                GRID[p.getPosX()][p.getPosY()] = p;

            }
        }

        if (blackPlayer != null) {
            for (Piece p : blackPlayer.getPieces()) {
                GRID[p.getPosX()][p.getPosY()] = p;
            }
        }
    }

    public static Piece getPieceFromBoard(int posX, int posY) {
        return GRID[posX][posY];
    }

    public static void printBoard() {
        System.out.println("\n    a  b  c  d  e  f  g  h");
        System.out.println("   -------------------------");

        for (int x = 0; x < 8; x++) {
            System.out.print((8 - x) + " |");

            for (int y = 0; y < 8; y++) {
                Piece p = GRID[x][y];
                System.out.print(p == null ? " . " : " " + p + " ");
            }
            
            System.out.println("| " + (8 - x));
        }
        
        System.out.println("   -------------------------");
        System.out.println("    a  b  c  d  e  f  g  h");
    }
}
