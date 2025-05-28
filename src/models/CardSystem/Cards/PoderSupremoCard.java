package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.enums.PieceType;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;
import java.util.function.Predicate;

public class PoderSupremoCard extends AbstractCard {
    public PoderSupremoCard() {
        super("Poder Supremo", RarityType.LENDARIA, "Elimine qualquer peca do tabuleiro imediatamente (Exceto rei ou rainha)", 6);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha uma peça no tabuleiro para eliminar (exceto Rei ou Rainha):");
        
        Predicate<Piece> filter = p -> p.getType() != PieceType.KING && p.getType() != PieceType.QUEEN;
        Optional<Piece> selectedPieceOpt = models.GameLogic.requestPieceSelectionFromBoard("Selecione a peça para eliminar:", filter);

        if (selectedPieceOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma peça válida selecionada. Ação cancelada.");
            return false;
        }
        Piece targetPiece = selectedPieceOpt.get();
        
        models.GameLogic.removePieceFromGame(targetPiece); 
        // A mensagem já é exibida por removePieceFromGame
        return true;
    }
}