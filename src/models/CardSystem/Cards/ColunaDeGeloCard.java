package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.RarityType;
import java.util.Optional;

public class ColunaDeGeloCard extends AbstractCard {
    public ColunaDeGeloCard() {
        super("Coluna de gelo", RarityType.RARA, "Uma coluna inteira do tabuleiro nao pode ser usada por 2 turnos", 3);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha a coluna para congelar (ex: 'a' até 'h'):");
        Optional<Point> coordOpt = models.GameLogic.requestCoordinateInput("Digite qualquer coordenada na coluna desejada (ex: a1 para coluna 'a'):");

        if (coordOpt.isEmpty()) {
            models.GameLogic.displayMessage("Nenhuma coluna selecionada. Ação cancelada.");
            return false;
        }
        int columnIndex = coordOpt.get().y; // y é a coluna
        
        int durationInFullTurns = 2; // 2 turnos completos
        models.Board.activateIceColumn(columnIndex, durationInFullTurns);
        return true;
    }
}