package models.CardSystem.Cards;

import models.AbstractCard;
import models.Player;
import models.Point;
import models.enums.RarityType;
import models.pieces.Piece;
import java.util.Optional;

public class TrocaEstrategicaCard extends AbstractCard {
    public TrocaEstrategicaCard() {
        super("Troca Estrategica", RarityType.RARA, "Troque de lugar duas pecas suas", 3);
    }

    @Override
    protected boolean applyEffect(Player activatingPlayer) {
        models.GameLogic.displayMessage("Escolha a primeira peça sua para trocar:");
        Optional<Piece> piece1Opt = models.GameLogic.requestPieceSelectionFromPlayer(activatingPlayer, "Selecione a primeira peça:", p -> true);
        if (piece1Opt.isEmpty()) {
            models.GameLogic.displayMessage("Seleção da primeira peça cancelada.");
            return false;
        }
        Piece piece1 = piece1Opt.get();
        String p1PosStr = models.GameLogic.coordsToAlgebraic(piece1.getPosX(), piece1.getPosY());

        models.GameLogic.displayMessage("Escolha a segunda peça sua para trocar com " + piece1.getType() + " em " + p1PosStr + ":");
        Optional<Piece> piece2Opt = models.GameLogic.requestPieceSelectionFromPlayer(activatingPlayer, "Selecione a segunda peça:", p -> p != piece1);
        if (piece2Opt.isEmpty()) {
            models.GameLogic.displayMessage("Seleção da segunda peça cancelada.");
            return false;
        }
        Piece piece2 = piece2Opt.get();

        Point pos1 = new Point(piece1.getPosX(), piece1.getPosY());
        Point pos2 = new Point(piece2.getPosX(), piece2.getPosY());
        
        Point lastPosForPiece1AfterSwap = pos1; 
        Point lastPosForPiece2AfterSwap = pos2; 


        piece1.setPosX(pos2.x);
        piece1.setPosY(pos2.y);
        piece1.setLastPosition(lastPosForPiece1AfterSwap);

        piece2.setPosX(pos1.x);
        piece2.setPosY(pos1.y);
        piece2.setLastPosition(lastPosForPiece2AfterSwap);
        
        models.Board.setPiece(pos1.x, pos1.y, piece2); 
        models.Board.setPiece(pos2.x, pos2.y, piece1); 
        models.Board.updateBoard(); 

        models.GameLogic.displayMessage("Peças " + piece1.getType() + " e " + piece2.getType() + " trocaram de lugar!");
        return true;
    }
}
