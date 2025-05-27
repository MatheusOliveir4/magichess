package models;

import models.enums.Color;
import models.pieces.Piece;

public class GameLogic {
    
    
    private static Player whitePlayer;
    private static Player blackPlayer;

    private static Player actualPlayer;

    public static void startGame() {
        System.out.println(Board.TITLE_ART);

        new Board();

        whitePlayer = new Player(Color.WHITE, true);
        initializePlayerPieces(Color.WHITE, whitePlayer);
        blackPlayer = new Player(Color.BLACK, false);
        initializePlayerPieces(Color.BLACK, blackPlayer);

        actualPlayer = whitePlayer;
        whitePlayer.setPlayerTurn(true);
        blackPlayer.setPlayerTurn(false);
}

    public static void initializePlayerPieces(Color color, Player player) {
        int row = (color == Color.WHITE) ? 7 : 0;

        player.addPieces(Board.getPieceFromBoard(row, 0));
        player.addPieces(Board.getPieceFromBoard(row, 1));
        player.addPieces(Board.getPieceFromBoard(row, 2));
        player.addPieces(Board.getPieceFromBoard(row, 3));
        player.addPieces(Board.getPieceFromBoard(row, 4));
        player.addPieces(Board.getPieceFromBoard(row, 5));
        player.addPieces(Board.getPieceFromBoard(row, 6));
        player.addPieces(Board.getPieceFromBoard(row, 7));

        row = (color == Color.WHITE) ? 6 : 1;

        for (int i = 0; i < 8; i++) {
            player.addPieces(Board.getPieceFromBoard(row, i));
        }
    }

    public static void movePiece(int posX, int posY, int newX, int newY) throws Exception {
        if (posX < 0 || posX >= 8 || posY < 0 || posY >= 8 || newX < 0 || newX >= 8 || newY < 0 || newY >= 8) {
            throw new Exception("Posicoes invalidas");
        }
    
        Piece piece = Board.getPieceFromBoard(posX, posY);
        Piece destinyPlace = Board.getPieceFromBoard(newX, newY);

        if (piece == null ) {
            throw new Exception("Selecione uma peca valida");
        }

        // Verifica se a peça pertence ao jogador do turno
        if (piece.getColor() != actualPlayer.getColor()) {
            throw new Exception("Não é o seu turno!");
        }

        piece.movement(newX, newY, destinyPlace);  

        if (destinyPlace != null) {
            Player opponent = (actualPlayer.getColor() == Color.WHITE) ? blackPlayer : whitePlayer;
            boolean removido  =  opponent.getPieces().remove(destinyPlace);
        }
        
        Board.updateBoard();
        swapPlayer();
    }

    public static void swapPlayer() {
        if (actualPlayer.getColor() == Color.WHITE) {
            actualPlayer = blackPlayer;
            whitePlayer.setPlayerTurn(false);
            blackPlayer.setPlayerTurn(true);
        } else {
            actualPlayer = whitePlayer;
            whitePlayer.setPlayerTurn(true);
            blackPlayer.setPlayerTurn(false);
        }
}
    
    public static boolean isGameOver() {
        return false;
    }
    
    public static void printGame() {
        Board.printBoard();
        System.out.println("\nVez de jogar: " + (actualPlayer.getColor() == Color.WHITE ? "Brancas" : "Pretas"));
        printHelp();
}

    public static Player getWhitePlayer() {
        return whitePlayer;
    }

    public static Player getBlackPlayer() {
        return blackPlayer;
    }

    public static void printHelp() {
        System.out.println("\nComandos disponiveis:");
        System.out.println("  move <origem> <destino> - Move uma peca (ex: move a2 a4)");
        System.out.println("  use <numero>            - Usa a carta da sua mao com o número especificado");
        System.out.println("  help                    - Mostra esta ajuda");
        System.out.println("  exit                    - Sai do jogo");
    }
}
