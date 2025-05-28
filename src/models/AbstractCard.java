package models;

import models.enums.RarityType;

public abstract class AbstractCard {
    protected String name;
    protected RarityType rarityType;
    protected String effectDescription;
    protected int baseCooldown;
    protected int currentCooldown;

    public AbstractCard(String name, RarityType rarityType, String effectDescription, int baseCooldown) {
        this.name = name;
        this.rarityType = rarityType;
        this.effectDescription = effectDescription;
        this.baseCooldown = baseCooldown;
        this.currentCooldown = 0;
    }

    public String getName() { return name; }
    public RarityType getRarityType() { return rarityType; }
    public String getEffectDescription() { return effectDescription; }
    public int getBaseCooldown() { return baseCooldown; }
    public int getCurrentCooldown() { return currentCooldown; }

    public boolean canBeUsed(Player player) {
        if (currentCooldown > 0) return false;
        if (player.getCategoryCooldownTurnsLeft(this.rarityType) > 0) return false;
        if (player.getTurnsSilenced() > 0) return false;
        return true;
    }

    public String getCooldownStatus(Player player) {
        if (currentCooldown > 0) {
            return "Carta em cooldown individual: " + currentCooldown + " turno(s) desta carta.";
        }
        if (player.getCategoryCooldownTurnsLeft(this.rarityType) > 0) {
            return "Cooldown de categoria " + this.rarityType + ": " + player.getCategoryCooldownTurnsLeft(this.rarityType) + " turno(s) do jogador.";
        }
        if (player.getTurnsSilenced() > 0) {
            return "Não pode usar cartas: Silêncio Real ativo por " + player.getTurnsSilenced() + " meio(s) de turno.";
        }
        return "Pronta para uso.";
    }

    protected void applyStandardCooldowns(Player activatingPlayer) {
        this.currentCooldown = this.baseCooldown;
        int categoryCooldownDuration = 0;
        switch (this.rarityType) {
            case COMUM:
                categoryCooldownDuration = 1;
                break;
            case RARA:
                categoryCooldownDuration = 3;
                break;
            case LENDARIA:
                categoryCooldownDuration = 6;
                break;
        }
        activatingPlayer.setCategoryCooldownTurnsLeft(this.rarityType, categoryCooldownDuration);
    }

    public void decrementIndividualCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    public final boolean activate(Player activatingPlayer) {
        if (!canBeUsed(activatingPlayer)) {
            models.GameLogic.displayMessage("Carta '" + getName() + "' não pode ser usada agora. Status: " + getCooldownStatus(activatingPlayer));
            return false;
        }

        models.GameLogic.displayMessage(activatingPlayer.getName() + " está tentando usar a carta: " + getName() + " - " + getEffectDescription());
        boolean effectSuccessfullyApplied = applyEffect(activatingPlayer);

        if (effectSuccessfullyApplied) {
            applyStandardCooldowns(activatingPlayer);
            models.GameLogic.displayMessage("Carta '" + getName() + "' usada com sucesso por " + activatingPlayer.getName() + "!");
        } else {
            models.GameLogic.displayMessage("Falha ao aplicar o efeito da carta '" + getName() + "'. A carta não foi consumida.");
        }
        return effectSuccessfullyApplied;
    }

    protected abstract boolean applyEffect(Player activatingPlayer);

    @Override
    public String toString() {
        String status = "";
        if (currentCooldown > 0) {
            status = " (Cooldown: " + currentCooldown + ")";
        } else {
            
        }
        return String.format("[%s] %s: %s%s", rarityType, name, effectDescription, status);
    }
}