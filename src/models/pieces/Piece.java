package models.pieces;

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
     private boolean caminhoLivre(int fromX, int fromY, int toX, int toY) {
        int dx = Integer.compare(toX, fromX);
        int dy = Integer.compare(toY, fromY);
        int x = fromX + dx, y = fromY + dy;
        while (x != toX || y != toY) {
            if (grid[x][y] != null) return false;
            x += dx;
            y += dy;
        }
        return true;
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
}
