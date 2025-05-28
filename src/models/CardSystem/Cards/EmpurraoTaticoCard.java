package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.PieceType;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;
import java.util.function.Predicate;

public class EmpurraoTaticoCard extends AbstractCard {
    public EmpurraoTaticoCard() {
        super("Empurrao Tatico", RarityType.COMUM, "Avance um peao duas casas", 1);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha um dos seus peões para avançar duas casas (ex: a2):");
        
        Predicate<Piece> pawnFilter = p -> p.getType() == PieceType.PAWN;
        Optional<Piece> selectedPieceOpt = models.GameLogic.requestPieceSelectionFromPlayer(activatingPlayer, "Selecione o peão:", pawnFilter);

        if (selectedPieceOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhum peão selecionado. Ação cancelada.");
            return false;
        }
        Piece pawn = selectedPieceOpt.get();

        int direction = (activatingPlayer.getColor() == models.enums.Color.WHITE) ? -1 : 1;
        int currentX = pawn.getPosX(); // Linha
        int currentY = pawn.getPosY(); // Coluna
        int targetX = currentX + (2 * direction);

        if (!models.GameLogic.isValidBoardPosition(targetX, currentY)) {
            models.GameLogic.displayMessage("Movimento inválido: Fora do tabuleiro.");
            return false;
        }
        
        Piece pieceAtStepOne = models.Board.getPieceFromBoard(currentX + direction, currentY);
        Piece pieceAtStepTwo = models.Board.getPieceFromBoard(targetX, currentY);

        if (pieceAtStepOne != null || pieceAtStepTwo != null){
            models.GameLogic.displayMessage("Movimento inválido: Caminho ou destino obstruído.");
            return false;
        }

        Point oldPos = new Point(currentX, currentY);
        pawn.setLastPosition(oldPos); 

        pawn.setPosX(targetX);

        models.Board.setPiece(oldPos.x, oldPos.y, null);
        models.Board.setPiece(targetX, currentY, pawn);
        models.Board.updateBoard();

        models.GameLogic.displayMessage("Peão em " + models.GameLogic.coordsToAlgebraic(oldPos.x, oldPos.y) +
                                     " avançou para " + models.GameLogic.coordsToAlgebraic(targetX, currentY) + "!");
        return true;
    }
}