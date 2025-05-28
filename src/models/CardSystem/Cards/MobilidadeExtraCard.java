package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;

public class MobilidadeExtraCard extends AbstractCard {
    public MobilidadeExtraCard() {
        super("Mobilidade Extra", RarityType.COMUM, "Uma peca pode se mover novamente (sem capturar)", 1);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha uma de suas peças para mover novamente (não pode capturar):");
        Optional<Piece> selectedPieceOpt = models.GameLogic.requestPieceSelectionFromPlayer(activatingPlayer, "Selecione a peça:", p -> true);

        if (selectedPieceOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma peça selecionada. Ação cancelada.");
            return false;
        }
        Piece pieceToMove = selectedPieceOpt.get();
        String originalPosStr = models.GameLogic.coordsToAlgebraic(pieceToMove.getPosX(), pieceToMove.getPosY());

        models.GameLogic.displayMessage("Escolha a casa de destino para " + pieceToMove.getType() + " em " + originalPosStr + ":");
        Optional<Point> targetCoordOpt = models.GameLogic.requestCoordinateInput("Digite a coordenada de destino (ex: a3):");

        if (targetCoordOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma coordenada de destino fornecida. Ação cancelada.");
            return false;
        }
        Point targetCoord = targetCoordOpt.get();

        Piece pieceAtTarget = models.Board.getPieceFromBoard(targetCoord.x, targetCoord.y);
        if (pieceAtTarget != null) {
            models.GameLogic.displayMessage("Movimento inválido: A casa de destino deve estar vazia (não pode capturar).");
            return false;
        }

        Point oldPos = new Point(pieceToMove.getPosX(), pieceToMove.getPosY());
        try {

            if (models.GameLogic.attemptSpecialMove(pieceToMove, targetCoord.x, targetCoord.y, false)) { // false = não permite captura
                models.GameLogic.displayMessage("Peça " + pieceToMove.getType() + " movida de " + originalPosStr +
                                         " para " + models.GameLogic.coordsToAlgebraic(targetCoord.x, targetCoord.y) + "!");
                return true;
            } else {
                models.GameLogic.displayMessage("Não foi possível mover a peça para o destino (movimento inválido para a peça ou outra restrição).");
                return false;
            }
        } catch (Exception e) { 
            models.GameLogic.displayMessage("Não foi possível mover a peça: " + e.getMessage());
            pieceToMove.setPosX(oldPos.x);
            pieceToMove.setPosY(oldPos.y);
            models.Board.setPiece(targetCoord.x, targetCoord.y, null); // Limpa o destino se algo foi colocado
            models.Board.setPiece(oldPos.x, oldPos.y, pieceToMove); // Coloca de volta
            models.Board.updateBoard();
            return false;
        }
    }
}