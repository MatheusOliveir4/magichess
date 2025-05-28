package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.PieceType;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;
import java.util.function.Predicate;

public class BloqueioTaticoCard extends AbstractCard {
    public BloqueioTaticoCard() {
        super("Bloqueio Tatico", RarityType.COMUM, "Uma peca inimiga nao pode ser movida por um turno (exceto rei)", 1);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        Player opponent = models.GameLogic.getOpponentPlayer(activatingPlayer);
        models.GameLogic.displayMessage("Escolha uma peça do oponente ("+opponent.getName()+") para bloquear (exceto o Rei):");

        Predicate<Piece> enemyPieceFilter = p -> p.getColor() == opponent.getColor() && p.getType() != PieceType.KING;
        Optional<Piece> selectedPieceOpt = models.GameLogic.requestPieceSelectionFromBoard("Selecione a peça inimiga:", enemyPieceFilter);

        if (selectedPieceOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma peça inimiga válida selecionada. Ação cancelada.");
            return false;
        }
        Piece targetPiece = selectedPieceOpt.get();

        targetPiece.setTurnsBlocked(2); // 2 meios-turnos = 1 turno completo do oponente
        models.GameLogic.displayMessage("Peça " + targetPiece.getType() + " do oponente em " +
                                 models.GameLogic.coordsToAlgebraic(targetPiece.getPosX(), targetPiece.getPosY()) +
                                 " bloqueada por 1 turno.");
        return true;
    }
}