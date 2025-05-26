package models;

import models.enums.Color;
import models.pieces.Piece;

public class GameLogic {
    public static Player getWhitePlayer() {
        return whitePlayer;
    }

    public static Player getBlackPlayer() {
        return blackPlayer;
    }
    
    private static Player whitePlayer;
    private static Player blackPlayer;

    private static Player actualPlayer;

    public static void startGame() {
        System.out.println(Board.TITLE_ART);
        new Board();

        whitePlayer = new Player(Color.WHITE, true);
        blackPlayer = new Player(Color.BLACK, false);
    
        actualPlayer = whitePlayer;
    }

    public static void movePiece(int posX, int posY, int newX, int newY) {
        if (posX < 0 || posX >= 8 || posY < 0 || posY >= 8 || newX < 0 || newX >= 8 || newY < 0 || newY >= 8) {
            throw new Error("Posicoes invalidas");
        }
       
        Piece piece = Board.getPieceFromBoard(posX, posY);
        Piece destinyPlace = Board.getPieceFromBoard(newX, newY);

        if (piece == null || actualPlayer.getColor() != piece.getColor()) {
            throw new Error("Selecione uma peca valida");
        }

        piece.movement(newX, newY, destinyPlace);   
        Board.updateBoard();
        swapPlayer();
    }

    public static void swapPlayer() {
        actualPlayer = (actualPlayer.getColor() == Color.WHITE) ? blackPlayer : whitePlayer;
    }
    
    public static void gameOver() {
        
    }
    
    public static void printGame() {
        Board.printBoard();
        printHelp();
    }

    private static void printHelp() {
        System.out.println("\nComandos disponiveis:");
        System.out.println("  move <origem> <destino> - Move uma peca (ex: move a2 a4)");
        System.out.println("  use <numero>            - Usa a carta da sua mao com o número especificado");
        System.out.println("  pass / endturn          - Passa o turno");
        System.out.println("  help                    - Mostra esta ajuda");
        System.out.println("  exit                    - Sai do jogo");
    }
}
