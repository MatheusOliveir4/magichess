package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;
import java.util.function.Predicate;

public class RecuoSeguroCard extends AbstractCard {
    public RecuoSeguroCard() {
        super("Recuo Seguro", RarityType.COMUM, "Volte uma peca sua a posicao anterior", 1);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha uma de suas peças para recuar para a posição anterior:");
        
        Predicate<Piece> hasMovedFilter = p -> p.getLastPosition() != null;
        Optional<Piece> selectedPieceOpt = models.GameLogic.requestPieceSelectionFromPlayer(activatingPlayer, "Selecione a peça:", hasMovedFilter);

        if (selectedPieceOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma peça válida selecionada ou sem posição anterior. Ação cancelada.");
            return false;
        }
        Piece pieceToMove = selectedPieceOpt.get();
        Point lastPos = pieceToMove.getLastPosition();

        if (lastPos == null) { 
            models.GameLogic.displayMessage("A peça selecionada não tem uma posição anterior registrada.");
            return false;
        }
        
        Piece pieceAtLastPos = models.Board.getPieceFromBoard(lastPos.x, lastPos.y);
        if (pieceAtLastPos != null) {
             models.GameLogic.displayMessage("A posição anterior ("+models.GameLogic.coordsToAlgebraic(lastPos.x, lastPos.y)+") está ocupada.");
             return false;
        }

        Point currentPos = new Point(pieceToMove.getPosX(), pieceToMove.getPosY());
        
        pieceToMove.setPosX(lastPos.x);
        pieceToMove.setPosY(lastPos.y);
        pieceToMove.setLastPosition(currentPos); 

        models.Board.setPiece(currentPos.x, currentPos.y, null);
        models.Board.setPiece(lastPos.x, lastPos.y, pieceToMove);
        models.Board.updateBoard();

        models.GameLogic.displayMessage("Peça " + pieceToMove.getType() + " recuou para " + models.GameLogic.coordsToAlgebraic(lastPos.x, lastPos.y) + "!");
        return true;
    }
}