package models.pieces;

import java.util.Objects;

import models.enums.Color;
import models.enums.PieceType;

public abstract class Piece {
    private int posX;
    private int posY;
    private Color color;
    private PieceType pieceSurname;

    public Piece(int posX, int posY, Color color, PieceType pieceSurname) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.pieceSurname = pieceSurname;
    }
    
    public abstract void movement(int newX, int newY, Piece destinyPlace);

    @Override public String toString() {
        return pieceSurname.getSymbol(this.color);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public PieceType getPieceSurname() {
        return pieceSurname;
    }

    public void setPieceSurname(PieceType pieceSurname) {
        this.pieceSurname = pieceSurname;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Piece piece = (Piece) obj;

        return this.posX == piece.posX &&
            this.posY == piece.posY &&
            this.color == piece.color &&
            this.pieceSurname == piece.pieceSurname;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY, color, pieceSurname);
    }

}
