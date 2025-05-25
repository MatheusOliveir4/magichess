package models.pieces;

import models.enums.Color;
import models.enums.PieceType;

public class Knight extends Piece {

    public Knight(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'movement'");
    }
    
}
