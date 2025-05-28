package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.RarityType;

public class ReflexoRealCard extends AbstractCard {
    public ReflexoRealCard() {
        super("Reflexo Real", RarityType.RARA, "Se uma peca sua for capturada, a peca inimiga tambem eh destruida", 3);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        activatingPlayer.setReflexoRealActive(true);
        models.GameLogic.displayMessage("Reflexo Real ativado para " + activatingPlayer.getName() + "! Se uma peça sua for capturada, a peça atacante também será destruída.");
        return true;
    }
}