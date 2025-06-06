package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.RarityType;

public class TratoFeitoCard extends AbstractCard {
    public TratoFeitoCard() {
        super("Trato Feito", RarityType.RARA, "Compre duas cartas.", 3);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.drawNewCardForPlayer(activatingPlayer, 2);
        // A mensagem já é exibida por drawNewCardForPlayer
        return true;
    }
}