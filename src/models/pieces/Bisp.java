package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class Bisp extends Piece {

    public Bisp(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int dx = newX - this.getPosX();
        int dy = newY - this.getPosY();

        if (Math.abs(dx) == Math.abs(dy) && destinyPlace == null) {
            this.setPosX(newX);
            this.setPosY(newY);
        } else {
            throw new Error("Movimento inválido para o Bispo na posição: (" + getPosX() + ", " + getPosY() + ")");
        }
    }
    
}
