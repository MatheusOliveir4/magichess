package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class Knight extends Piece {

    public Knight(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int dx = newX - this.getPosX();
        int dy = newY - this.getPosY();
        if ((Math.abs(dx)== 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2)) {
            if (destinyPlace == null || destinyPlace.getColor() != this.getColor()) {
                this.setPosX(newX);
                this.setPosY(newY);
            }
        } else {
            throw new Error("Movimento inválido para o Cavalo na posição: (" + getPosX() + ", " + getPosY() + ")");
        }
  
    }
    
}
