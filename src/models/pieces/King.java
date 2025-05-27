package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class King extends Piece {


    public King(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int dx = newX - this.getPosX();
        int dy = newY - this.getPosY();
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1 && (dx != 0 || dy != 0)) {
            if (destinyPlace == null || destinyPlace.getColor() != this.getColor()) {
                this.setPosX(newX);
                this.setPosY(newY);
            }
        } else {
            throw new IllegalStateException("Movimento inválido para o Rei na posição: (" + getPosX() + ", " + getPosY() + ")");
        }
    }
}


