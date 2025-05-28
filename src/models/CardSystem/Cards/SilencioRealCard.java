package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.RarityType;

public class SilencioRealCard extends AbstractCard {
    public SilencioRealCard() {
        super("Silencio Real", RarityType.RARA, "Ninguem pode usar cartas por 2 turnos", 3);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        int durationHalfTurns = 4; // 2 turnos completos = 4 meios-turnos
        activatingPlayer.setTurnsSilenced(durationHalfTurns);
        models.GameLogic.getOpponentPlayer(activatingPlayer).setTurnsSilenced(durationHalfTurns);
        models.GameLogic.displayMessage("Silêncio Real ativado! Nenhum jogador poderá usar cartas por 2 turnos completos.");
        return true;
    }
}