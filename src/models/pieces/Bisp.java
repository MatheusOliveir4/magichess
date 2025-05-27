package models.pieces;

import models.Board;
import models.enums.Color;
import models.enums.PieceType;

public class Bisp extends Piece {

    public Bisp(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int currentX = this.getPosX();
        int currentY = this.getPosY();
        int dx = newX - currentX;
        int dy = newY - currentY;

        if (Math.abs(dx) != Math.abs(dy) || dx == 0) {
            throw new IllegalArgumentException("Movimento inválido para o Bispo na posição: (" + currentX + ", " + currentY + ")");
        }

        int stepX = dx > 0 ? 1 : -1;
        int stepY = dy > 0 ? 1 : -1;

        int x = currentX + stepX;
        int y = currentY + stepY;

        
        while (x != newX && y != newY) {
            if (Board.getPieceFromBoard(x, y) != null) {
                throw new IllegalStateException("Caminho bloqueado por peça em: (" + x + ", " + y + ")");
            }
            x += stepX;
            y += stepY;
        }

        
        Piece targetPiece = Board.getPieceFromBoard(newX, newY);
        if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
            this.setPosX(newX);
            this.setPosY(newY);
        } else {
            throw new IllegalStateException("Não pode capturar uma peça da mesma cor.");
        }
    }
}
