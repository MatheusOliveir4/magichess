package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.RarityType;

public class BarreiraImperialCard extends AbstractCard {
    public BarreiraImperialCard() {
        super("Barreira Imperial", RarityType.LENDARIA, "Suas pecas nao podem ser capturadas no proximo turno", 6);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        int durationInOpponentTurns = 1; // "Próximo turno" = 1 turno completo do oponente
        models.Board.activateImperialBarrier(activatingPlayer, durationInOpponentTurns);
        // A mensagem já é exibida por activateImperialBarrier
        return true;
    }
}