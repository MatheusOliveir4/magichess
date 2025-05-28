package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.PieceType;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RenascimentoCard extends AbstractCard {
    public RenascimentoCard() {
        super("Renascimento", RarityType.LENDARIA, "Reviva uma peca sua capturada e coloque-a em sua linha inicial (exceto a rainha)", 6);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        List<Piece> capturedPlayerPieces = activatingPlayer.getCapturedOwnPieces();
        if (capturedPlayerPieces.isEmpty()) {
            models.GameLogic.displayMessage(activatingPlayer.getName() + " não tem peças capturadas para reviver.");
            return false;
        }

        List<PieceType> reviveableTypes = capturedPlayerPieces.stream()
            .map(Piece::getType)
            .filter(type -> type != PieceType.QUEEN)
            .distinct()
            .collect(Collectors.toList());

        if (reviveableTypes.isEmpty()) {
            models.GameLogic.displayMessage(activatingPlayer.getName() + " não tem peças capturadas elegíveis (não-Rainha) para Renascimento.");
            return false;
        }

        Optional<PieceType> selectedTypeOpt = models.GameLogic.requestPieceTypeSelection(
            activatingPlayer, 
            "Escolha um tipo de peça sua capturada para reviver (exceto Rainha):", 
            reviveableTypes
        );
        
        if (selectedTypeOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhum tipo de peça selecionado. Ação cancelada.");
            return false;
        }
        PieceType typeToRevive = selectedTypeOpt.get();

        // Encontrar uma casa vazia na linha inicial do jogador
        // Linha inicial: Brancas X=7 (linha '1' no display, se '8' é X=0), Pretas X=0 (linha '8' no display)
        int initialRowX = (activatingPlayer.getColor() == models.enums.Color.WHITE) ? 7 : 0;
        Optional<Point> placementPosOpt = Optional.empty();

        for (int y = 0; y < 8; y++) { // Itera pelas colunas y
            if (models.Board.isPositionEmpty(initialRowX, y)) {
                placementPosOpt = Optional.of(new Point(initialRowX, y));
                break;
            }
        }

        if (placementPosOpt.isEmpty()) {
            models.GameLogic.displayMessage("Não há casas vazias na linha inicial para reviver a peça " + typeToRevive + ".");
            return false;
        }
        Point placementPos = placementPosOpt.get();
        
        boolean success = models.GameLogic.revivePieceForPlayer(activatingPlayer, typeToRevive, placementPos.x, placementPos.y);

        if (success) {
            models.GameLogic.displayMessage(typeToRevive + " revivido para " + activatingPlayer.getName() + " em " +
                                 models.GameLogic.coordsToAlgebraic(placementPos.x, placementPos.y) + "!");
            return true;
        } else {
            models.GameLogic.displayMessage("Falha ao tentar reviver a peça (ex: não encontrada na lista de capturadas ou erro ao criar).");
            return false;
        }
    }
}