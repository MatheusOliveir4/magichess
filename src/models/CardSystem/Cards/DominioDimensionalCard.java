package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;

public class DominioDimensionalCard extends AbstractCard {
    public DominioDimensionalCard() {
        super("Dominio Dimensional", RarityType.LENDARIA, "Teleporte qualquer peca sua para qualquer casa vazia", 6);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha uma de suas peças para teleportar:");
        Optional<Piece> selectedPieceOpt = models.GameLogic.requestPieceSelectionFromPlayer(activatingPlayer, "Selecione a peça:", _ -> true);

        if (selectedPieceOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma peça selecionada. Ação cancelada.");
            return false;
        }
        Piece pieceToTeleport = selectedPieceOpt.get();
        String originalPosStr = models.GameLogic.coordsToAlgebraic(pieceToTeleport.getPosX(), pieceToTeleport.getPosY());

        models.GameLogic.displayMessage("Escolha uma casa vazia no tabuleiro para teleportar " + pieceToTeleport.getType() + " de " + originalPosStr + ":");
        Optional<Point> targetCoordOpt = models.GameLogic.requestCoordinateInput("Digite a coordenada de destino (ex: e5):");

        if (targetCoordOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma coordenada de destino fornecida. Ação cancelada.");
            return false;
        }
        Point targetCoord = targetCoordOpt.get();

        if (!models.Board.isPositionEmpty(targetCoord.x, targetCoord.y)) {
            models.GameLogic.displayMessage("A casa de destino (" + models.GameLogic.coordsToAlgebraic(targetCoord.x, targetCoord.y) + ") não está vazia.");
            return false;
        }
        
        Point oldPos = new Point(pieceToTeleport.getPosX(), pieceToTeleport.getPosY());

        pieceToTeleport.setPosX(targetCoord.x);
        pieceToTeleport.setPosY(targetCoord.y);
        pieceToTeleport.setLastPosition(oldPos); // Define a última posição como de onde teleportou

        models.Board.setPiece(oldPos.x, oldPos.y, null); // Limpa a casa antiga
        models.Board.setPiece(targetCoord.x, targetCoord.y, pieceToTeleport); // Coloca na nova casa
        models.Board.updateBoard();

        models.GameLogic.displayMessage("Domínio Dimensional! Peça " + pieceToTeleport.getType() + " teleportada de " + originalPosStr +
                                     " para " + models.GameLogic.coordsToAlgebraic(targetCoord.x, targetCoord.y) + "!");
        return true;
    }
}