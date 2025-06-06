package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class Pawn extends Piece {

    private boolean firstMovement = false;

    public Pawn(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int dx = newX - this.getPosX(); 
        int dy = newY - this.getPosY(); 

        dx = (this.getColor() == Color.WHITE) ? dx * (-1) : dx;

        if (dx == 2 && dy == 0 && !firstMovement) {
            this.setPosX(newX);
            firstMovement = true;

        } else if (dx == 1 && dy == 0 && destinyPlace == null) {
            this.setPosX(newX);
            firstMovement = true;

        } else if (dx == 1 && Math.abs(dy) == 1 && destinyPlace != null && destinyPlace.getColor() != this.getColor()) {
            this.setPosX(newX);
            this.setPosY(newY);
            firstMovement = true;

            System.out.println(destinyPlace.getPosX() + " " + destinyPlace.getPosY() + " " + destinyPlace.getPieceSurname());

        } else {
            throw new IllegalStateException("Nao foi possivel mover peca na posicao: (" + getPosX() + ", " + getPosY() + ")");
        }
    }
    
}
