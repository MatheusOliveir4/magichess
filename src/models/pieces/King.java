package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class King extends Piece{

    public King(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int dx= this.getPosX();
        int dy= this.getPosY();
        if (Math.abs(newX - dx) <= 1 && Math.abs(newY - dy) <= 1) {
            if (destinyPlace == null || destinyPlace.getColor() != this.getColor()) {
                this.setPosX(newX);
                this.setPosY(newY);
            } else {
                throw new Error("Movimento inválido para o Rei na posição: (" + getPosX() + ", " + getPosY() + ")");
            }
        } else {
            throw new Error("Movimento inválido para o Rei na posição: (" + getPosX() + ", " + getPosY() + ")");
        }
    
    }
    
}
