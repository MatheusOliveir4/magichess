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

    if (piece.getColor() != actualPlayer.getColor()) {
        throw new Exception("Não é o seu turno!");
    }

    
    if (piece instanceof models.pieces.King && Math.abs(newY - posY) == 2 && posX == newX) {
        if (tentarRoque(piece, newX, newY)) {
            Board.updateBoard();
            swapPlayer();
            if (isKingInCheck(actualPlayer.getColor())) {
                System.out.println("Atenção: seu rei está em xeque!");
            }
            return;
        } else {
            throw new Exception("Roque não permitido!");
        }
    }

    if (moveLeavesKingInCheck(piece, posX, posY, newX, newY)) {
        throw new Exception("Movimento ilegal: seu rei ficaria em xeque!");
    }

    piece.movement(newX, newY, destinyPlace);  

    if (destinyPlace != null) {
        Player opponent = (actualPlayer.getColor() == Color.WHITE) ? blackPlayer : whitePlayer;
        opponent.getPieces().remove(destinyPlace);
    }
    
    Board.updateBoard();
    swapPlayer();

    if (isKingInCheck(actualPlayer.getColor())) {
        System.out.println("Atenção: seu rei está em xeque!");
    }
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
    public static boolean isKingInCheck(models.enums.Color kingColor) {
    Player player = (kingColor == Color.WHITE) ? whitePlayer : blackPlayer;
    Player opponent = (kingColor == Color.WHITE) ? blackPlayer : whitePlayer;

    Piece king = null;
    for (Piece p : player.getPieces()) {
        if (p instanceof models.pieces.King) {
            king = p;
            break;
        }
    }
    if (king == null) return false;

    int kingX = king.getPosX();
    int kingY = king.getPosY();

    for (Piece p : opponent.getPieces()) {
        try {
            p.movement(kingX, kingY, king);
            return true;
        } catch (Exception e) {
            
        }
    }
    return false;
}
    public static boolean moveLeavesKingInCheck(Piece piece, int fromX, int fromY, int toX, int toY) {
    Piece destinoOriginal = Board.getPiece(toX, toY);
    int oldX = piece.getPosX();
    int oldY = piece.getPosY();

    
    Board.setPiece(fromX, fromY, null);
    Board.setPiece(toX, toY, piece);
    piece.setPosX(toX);
    piece.setPosY(toY);

    boolean emXeque = isKingInCheck(piece.getColor());

    
    Board.setPiece(fromX, fromY, piece);
    Board.setPiece(toX, toY, destinoOriginal);
    piece.setPosX(oldX);
    piece.setPosY(oldY);

    return emXeque;
}
    public static boolean tentarRoque(Piece king, int newX, int newY) throws Exception {
    if (!(king instanceof models.pieces.King) || king.hasMoved()) return false;

    int dx = newX - king.getPosX();
    int dy = newY - king.getPosY();

    if (dx != 0 || Math.abs(dy) != 2) return false;

    int row = king.getPosX();
    int rookCol = (dy == 2) ? 7 : 0;
    Piece rook = Board.getPiece(row, rookCol);

    if (!(rook instanceof models.pieces.Tower) || rook.hasMoved()) return false;

    int start = Math.min(king.getPosY(), rookCol) + 1;
    int end = Math.max(king.getPosY(), rookCol) - 1;
    for (int y = start; y <= end; y++) {
        if (Board.getPiece(row, y) != null) return false;
    }

    int direction = (dy > 0) ? 1 : -1;
    for (int i = 0; i <= 2; i++) {
        int testY = king.getPosY() + i * direction;
        if (moveLeavesKingInCheck(king, row, king.getPosY(), row, testY)) {
            throw new Exception("Não é possível fazer o roque: rei passaria por xeque.");
        }
    }

    // Move o rei
    Board.setPiece(king.getPosX(), king.getPosY(), null);
    king.setPosY(king.getPosY() + 2 * direction);
    king.setHasMoved(true);
    Board.setPiece(king.getPosX(), king.getPosY(), king);

    // Move a torre
    Board.setPiece(row, rookCol, null);
    rook.setPosY(king.getPosY() - direction);
    rook.setHasMoved(true);
    Board.setPiece(row, rook.getPosY(), rook);

    return true;
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
