package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import models.enums.Color;
import models.enums.RarityType;
import models.enums.PieceType;
import models.pieces.Piece;

public class Player {
    private Color color;
    private boolean isPlayerTurn;
    private List<Piece> pieces = new ArrayList<>();
    private String name;

    // --- Adições para Cartas ---
    private List<AbstractCard> hand = new ArrayList<>();
    private Map<RarityType, Integer> categoryCooldowns = new HashMap<>();
    private boolean reflexoRealActive = false;
    private int turnsSilenced = 0;
    private List<Piece> capturedOwnPieces = new ArrayList<>(); // Peças DESTE jogador que foram capturadas pelo oponente
    // --- Fim Adições ---
    
    public Player(Color color, boolean isPlayerTurn, String name) {
        this.color = Objects.requireNonNull(color, "Player color cannot be null");
        this.isPlayerTurn = isPlayerTurn;
        this.name = Objects.requireNonNull(name, "Player name cannot be null");
        for (RarityType rarity : RarityType.values()) {
            categoryCooldowns.put(rarity, 0);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color, "Player color cannot be null");
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

    public void addPieces(Piece piece) {
        if (piece != null) {
            this.pieces.add(piece);
        }
    }
    
    public void removePiece(Piece piece) {
        if (piece != null) {
            this.pieces.remove(piece);
        }
    }

    public String getName() {
        return name;
    }

    // --- Métodos para Cartas ---
    public List<AbstractCard> getHand() {
        return hand;
    }

    public void addCardToHand(AbstractCard card) {
        if (card != null) {
            this.hand.add(card);
        }
    }

    public AbstractCard getCardFromHand(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.get(index);
        }
        return null;
    }

    public void removeCardFromHand(AbstractCard card) {
        if (card != null) {
            this.hand.remove(card);
        }
    }
    
    public int getCategoryCooldownTurnsLeft(RarityType rarityType) {
        return categoryCooldowns.getOrDefault(rarityType, 0);
    }

    public void setCategoryCooldownTurnsLeft(RarityType rarityType, int turns) {
        categoryCooldowns.put(rarityType, Math.max(0, turns));
    }

    public boolean isReflexoRealActive() {
        return reflexoRealActive;
    }

    public void setReflexoRealActive(boolean reflexoRealActive) {
        this.reflexoRealActive = reflexoRealActive;
    }

    public int getTurnsSilenced() {
        return turnsSilenced;
    }

    public void setTurnsSilenced(int turnsSilenced) {
        this.turnsSilenced = Math.max(0, turnsSilenced);
    }
    
    public List<Piece> getCapturedOwnPieces() {
        return capturedOwnPieces;
    }

    public void addCapturedOwnPiece(Piece piece) {
        if (piece != null && piece.getColor() == this.color) { // Garante que é uma peça DESTE jogador
            this.capturedOwnPieces.add(piece);
        }
    }
    
    public boolean removeRevivedPieceFromCapturedList(PieceType pieceType) {
        for (int i = 0; i < capturedOwnPieces.size(); i++) {
            if (capturedOwnPieces.get(i).getType() == pieceType) {
                capturedOwnPieces.remove(i);
                return true;
            }
        }
        return false;
    }

    public void decrementCooldownsAndEffects() {
        for (RarityType rarity : RarityType.values()) {
            setCategoryCooldownTurnsLeft(rarity, getCategoryCooldownTurnsLeft(rarity) - 1);
        }
        for (AbstractCard card : hand) {
            card.decrementIndividualCooldown();
        }
        if (turnsSilenced > 0) {
            turnsSilenced--;
        }
      
    }
   
}