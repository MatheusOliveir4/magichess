package models.pieces;

import java.util.Objects;

import models.enums.Color;
import models.enums.PieceType;
import models.Point; // Importar a classe Point

public abstract class Piece {
    private int posX;
    private int posY;
    private Color color;
    private PieceType pieceSurname; // Este é o seu PieceType
    protected boolean hasMoved = false;

    // --- NOVOS CAMPOS PARA CARTAS ---
    protected Point lastPosition; 
    protected int turnsBlocked = 0; 
    protected boolean affectedBySlipperySolo = false;
    // --- FIM NOVOS CAMPOS ---

    public Piece(int posX, int posY, Color color, PieceType pieceSurname) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.pieceSurname = pieceSurname;
        // this.hasMoved já é inicializado como false por padrão.
    }
    
    public abstract void movement(int newX, int newY, Piece destinyPlace) throws Exception; // Adicionado throws Exception para consistência

    @Override public String toString() {
        if (pieceSurname != null && this.color != null) {
            return pieceSurname.getSymbol(this.color);
        }
        return "?"; // Fallback
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

    // Adicionado para consistência com as cartas que usam getType()
    public PieceType getType() {
        return pieceSurname;
    }

    public void setPieceSurname(PieceType pieceSurname) {
        this.pieceSurname = pieceSurname;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    // --- NOVOS MÉTODOS PARA CARTAS ---
    public Point getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Point lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getTurnsBlocked() {
        return turnsBlocked;
    }

    public void setTurnsBlocked(int turns) {
        this.turnsBlocked = Math.max(0, turns); 
    }

    public void decrementTurnsBlocked() {
        if (this.turnsBlocked > 0) {
            this.turnsBlocked--;
        }
    }

    public boolean isAffectedBySlipperySolo() {
        return affectedBySlipperySolo;
    }

    public void setAffectedBySlipperySolo(boolean affected) {
        this.affectedBySlipperySolo = affected;
    }

    public void clearSlipperySoloEffect() { 
        this.affectedBySlipperySolo = false;
    }
    // --- FIM NOVOS MÉTODOS ---
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Piece piece = (Piece) obj;

        return this.posX == piece.posX &&
            this.posY == piece.posY &&
            this.color == piece.color &&
            this.pieceSurname == piece.pieceSurname &&
            this.hasMoved == piece.hasMoved; 
            
    }

    @Override
    public int hashCode() {

        return Objects.hash(posX, posY, color, pieceSurname, hasMoved);
    }
}