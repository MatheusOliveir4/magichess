package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class Tower extends Piece{

    public Tower(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
    int dx = newX - this.getPosX(); 
    int dy = newY - this.getPosY();

    
    if (((dx != 0 && dy == 0) || (dx == 0 && dy != 0))) {
        if (destinyPlace== null || destinyPlace.getColor() != this.getColor()){
        this.setPosX(newX);
        this.setPosY(newY);
        this.setHasMoved(true);
        }
    } else {
        throw new IllegalStateException("Movimento inválido para a Torre na posição: (" + getPosX() + ", " + getPosY() + ")");
    }
}

    }
    

