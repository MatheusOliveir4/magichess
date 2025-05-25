package models;

import java.util.ArrayList;
import java.util.List;

import models.enums.Color;

public class Player {
    private Color color;
    private boolean isPlayerTurn;
    private List<Piece> pieces = new ArrayList<>(); 
    
    public Player(Color color, boolean isPlayerTurn) {
        this.color = color;
        this.isPlayerTurn = isPlayerTurn;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }
}
