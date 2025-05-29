package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.RarityType;

public class SilencioRealCard extends AbstractCard {
    public SilencioRealCard() {
        super("Silencio Real", RarityType.RARA, "Ninguém pode usar cartas por 2 turnos", 3);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        int durationTurns = 2; // 2 turnos (cada jogador ficará silenciado por 2 turnos)
        activatingPlayer.setTurnsSilenced(durationTurns);
        models.GameLogic.getOpponentPlayer(activatingPlayer).setTurnsSilenced(durationTurns);
        models.GameLogic.displayMessage("Silêncio Real ativado! Nenhum jogador poderá usar cartas pelos próximos 2 turnos.");
        return true;
    }
}