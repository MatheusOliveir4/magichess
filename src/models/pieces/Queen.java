package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class Queen extends Piece{

    public Queen(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int dx = newX - this.getPosX();
        int dy = newY - this.getPosY();

         if (((dx != 0 && dy == 0) || (dx == 0 && dy != 0) || (Math.abs(dx) == Math.abs(dy))) && destinyPlace == null) {
                this.setPosX(newX);
                this.setPosY(newY);
          } else {
                throw new Error("Movimento inválido para a Rainha na posição: (" + getPosX() + ", " + getPosY() + ")");
          }        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'movement'");
    }
    
}
