package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.PieceType;
import models.enums.RarityType;
import models.pieces.Piece;

public class SoloEscorregadioCard extends AbstractCard {
    public SoloEscorregadioCard() {
        super("Solo Escorregadio", RarityType.COMUM, "Cavalo inimigo nao pode pular pecas neste turno", 1);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        Player opponent = models.GameLogic.getOpponentPlayer(activatingPlayer);
        if (opponent == null) {
            models.GameLogic.displayMessage("Não foi possível identificar o oponente.");
            return false;
        }

        boolean appliedToAnyKnight = false;
        for (Piece knight : opponent.getPieces()) {
            if (knight.getType() == PieceType.KNIGHT) {
                knight.setAffectedBySlipperySolo(true);
                appliedToAnyKnight = true;
            }
        }

        if (!appliedToAnyKnight) {
            models.GameLogic.displayMessage("Oponente não possui cavalos em jogo.");
        }
        models.GameLogic.displayMessage("Cavalos do oponente (" + opponent.getName() + ") não poderão pular peças no próximo turno deles.");
        return true;
    }
}