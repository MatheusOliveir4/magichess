package models;

import models.CardSystem.Cards.BarreiraImperialCard;
import models.CardSystem.Cards.BloqueioTaticoCard;
import models.CardSystem.Cards.ColunaDeGeloCard;
import models.CardSystem.Cards.DominioDimensionalCard;
import models.CardSystem.Cards.EmpurraoTaticoCard;
import models.CardSystem.Cards.MobilidadeExtraCard;
import models.CardSystem.Cards.PoderSupremoCard;
import models.CardSystem.Cards.RecuoSeguroCard;
import models.CardSystem.Cards.ReflexoRealCard;
import models.CardSystem.Cards.RenascimentoCard;
import models.CardSystem.Cards.SilencioRealCard;
import models.CardSystem.Cards.SoloEscorregadioCard;
import models.CardSystem.Cards.TratoFeitoCard;
import models.CardSystem.Cards.TrocaEstrategicaCard;
import models.enums.Color;
import models.enums.PieceType;
import models.enums.RarityType;
import models.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameLogic {
    private static Player whitePlayer;
    private static Player blackPlayer;
    private static Player actualPlayer;
    private static Scanner scanner = new Scanner(System.in);

    private static final List<Supplier<AbstractCard>> AVAILABLE_CARD_SUPPLIERS = new ArrayList<>();
    static {
        AVAILABLE_CARD_SUPPLIERS.add(EmpurraoTaticoCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(RecuoSeguroCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(MobilidadeExtraCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(SoloEscorregadioCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(BloqueioTaticoCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(ReflexoRealCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(TrocaEstrategicaCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(SilencioRealCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(ColunaDeGeloCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(TratoFeitoCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(PoderSupremoCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(BarreiraImperialCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(RenascimentoCard::new);
        AVAILABLE_CARD_SUPPLIERS.add(DominioDimensionalCard::new);
    }
    private static final int INITIAL_CARDS_PER_PLAYER = 2;

    public static void startGame() {
        System.out.println(Board.TITLE_ART);
        new Board();

        whitePlayer = new Player(Color.WHITE, true, "Jogador Branco");
        initializePlayerPieces(whitePlayer);
        
        blackPlayer = new Player(Color.BLACK, false, "Jogador Preto");
        initializePlayerPieces(blackPlayer);

        actualPlayer = whitePlayer;

        for (int i = 0; i < INITIAL_CARDS_PER_PLAYER; i++) {
            drawNewCardForPlayer(whitePlayer, 1);
            drawNewCardForPlayer(blackPlayer, 1);
        }
        Board.updateBoard();
    }
    
    public static void initializePlayerPieces(Player player) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = Board.getPieceFromBoard(r, c);
                if (p != null && p.getColor() == player.getColor()) {
                    player.addPieces(p);
                }
            }
        }
    }

    public static void movePiece(int fromX, int fromY, int toX, int toY) throws Exception {
        if (!isValidBoardPosition(fromX, fromY) || !isValidBoardPosition(toX, toY)) {
            throw new Exception("Posições inválidas. Linhas de 0-7 (display 8-1), Colunas de 0-7 (display a-h).");
        }

        Piece piece = Board.getPieceFromBoard(fromX, fromY);
        Piece destinyPlace = Board.getPieceFromBoard(toX, toY);

        if (piece == null ) {
            throw new Exception("Nenhuma peça na origem " + coordsToAlgebraic(fromX, fromY) + ".");
        }
        if (piece.getColor() != actualPlayer.getColor()) {
            throw new Exception("Não é sua vez de jogar com a peça em " + coordsToAlgebraic(fromX, fromY) + ".");
        }
        if (piece.getTurnsBlocked() > 0) {
            throw new Exception("Esta peça (" + piece.getType() + ") está bloqueada por mais " + piece.getTurnsBlocked() + " meio(s) de turno.");
        }
        if (Board.isColumnFrozen(fromY) || Board.isColumnFrozen(toY)) {
            throw new Exception("Esta coluna está congelada e não pode ser usada!");
        }

        if (piece instanceof King && Math.abs(toY - fromY) == 2 && fromX == toX) {
            if (tentarRoque((King)piece, toX, toY)) {
                Board.updateBoard(); 
                swapPlayer();
                checkAndAnnounceCheck();
                return;
            } else {
                 
                throw new Exception("Roque não permitido (condições não atendidas ou rei passaria por xeque).");
            }
        }
        

        Point oldPosition = new Point(piece.getPosX(), piece.getPosY());

        
        if (moveLeavesKingInCheck(piece, fromX, fromY, toX, toY)) {
            throw new Exception("Movimento ilegal: seu rei ("+actualPlayer.getColor()+") ficaria em xeque!");
        }

        
        Player opponent = getOpponentPlayer(actualPlayer);
        if (destinyPlace != null) {
            if (destinyPlace.getColor() == actualPlayer.getColor()) {
                throw new Exception("Não pode capturar sua própria peça em " + coordsToAlgebraic(toX, toY) + ".");
            }
            
            if (Board.isImperialBarrierActiveForPlayer(opponent)) {
                throw new Exception("Não é possível capturar: Barreira Imperial ativa para " + opponent.getName() + "!");
            }
        }

    
        try {
            piece.movement(toX, toY, destinyPlace);
            piece.setLastPosition(oldPosition);
        } catch (Exception e) {
            
            throw new Exception("Movimento inválido para a peça " + piece.getType() + ": " + e.getMessage());
        }

        
        if (destinyPlace != null && destinyPlace.getColor() == opponent.getColor()) {
            opponent.removePiece(destinyPlace);
            opponent.addCapturedOwnPiece(destinyPlace);
            displayMessage(actualPlayer.getName() + " capturou " + destinyPlace.getType() + " de " + opponent.getName() + ".");

        
            if (opponent.isReflexoRealActive()) {
                displayMessage("Reflexo Real ativado por " + opponent.getName() + "! A peça atacante " + piece.getType() + " de " + actualPlayer.getName() + " também é destruída!");
                removePieceFromGame(piece);
                opponent.setReflexoRealActive(false);
            }
        }
        
        Board.updateBoard();
        swapPlayer();
        checkAndAnnounceCheck();
    }
    
    private static void checkAndAnnounceCheck() {
        if (isKingInCheck(actualPlayer.getColor())) {
            displayMessage("Atenção: " + actualPlayer.getName() + ", seu rei está em XEQUE!");
            
            if (isCheckmate(actualPlayer)) {
                displayMessage("XEQUE-MATE! " + getOpponentPlayer(actualPlayer).getName() + " venceu!");
                
            }
        }
    }
    
    public static void swapPlayer() {
        processEndOfTurn(actualPlayer);

        actualPlayer.setPlayerTurn(false);
        actualPlayer = (actualPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        actualPlayer.setPlayerTurn(true);

        actualPlayer.setUsedCardThisTurn(false); // <-- Resetar uso de carta

        displayMessage("");
        displayMessage("--- Turno de " + actualPlayer.getName() + " (" + actualPlayer.getColor() + ") ---");

        boolean isFirstTurnOfBlack = (actualPlayer == blackPlayer) &&
                                    (blackPlayer.getHand().size() == INITIAL_CARDS_PER_PLAYER);
        if (!isFirstTurnOfBlack) {
            drawNewCardForPlayer(actualPlayer, 1);
        }
}
    
    public static boolean isKingInCheck(models.enums.Color kingColor) {
        Player playerWithKing = (kingColor == Color.WHITE) ? whitePlayer : blackPlayer;
        Player opponent = (kingColor == Color.WHITE) ? blackPlayer : whitePlayer;

        Piece king = null;
        for (Piece p : playerWithKing.getPieces()) {
            if (p instanceof King) {
                king = p;
                break;
            }
        }
        if (king == null) return false;

        int kingX = king.getPosX();
        int kingY = king.getPosY();

        for (Piece attackingPiece : opponent.getPieces()) {
            try {

                int attackerOldX = attackingPiece.getPosX();
                int attackerOldY = attackingPiece.getPosY();
                boolean attackerOldMoved = attackingPiece.hasMoved();
                Point attackerOldLastPos = attackingPiece.getLastPosition();

                attackingPiece.movement(kingX, kingY, king);

                
                attackingPiece.setPosX(attackerOldX);
                attackingPiece.setPosY(attackerOldY);
                attackingPiece.setHasMoved(attackerOldMoved);
                attackingPiece.setLastPosition(attackerOldLastPos);

                return true;
            } catch (Exception e) {
            
            }
        }
        return false;
    }
    
    public static boolean moveLeavesKingInCheck(Piece piece, int fromX, int fromY, int toX, int toY) {
        Piece originalDestinyPiece = Board.getPiece(toX, toY);
        Player pieceOwner = (piece.getColor() == Color.WHITE) ? whitePlayer : blackPlayer;
        Player opponent = getOpponentPlayer(pieceOwner);
        boolean wasDestinyPieceInOpponentList = false;
        if (originalDestinyPiece != null && originalDestinyPiece.getColor() == opponent.getColor()) {
            wasDestinyPieceInOpponentList = opponent.getPieces().contains(originalDestinyPiece);
        }

        Board.setPiece(fromX, fromY, null);
        Board.setPiece(toX, toY, piece);
        piece.setPosX(toX); 
        piece.setPosY(toY);
        
        if (originalDestinyPiece != null && originalDestinyPiece.getColor() != piece.getColor()) {
            getOpponentPlayer(pieceOwner).removePiece(originalDestinyPiece);
        }


        boolean isInCheck = isKingInCheck(piece.getColor());

        piece.setPosX(fromX);               
        piece.setPosY(fromY);
        Board.setPiece(fromX, fromY, piece);
        Board.setPiece(toX, toY, originalDestinyPiece);
        
        if (originalDestinyPiece != null && originalDestinyPiece.getColor() != piece.getColor() && wasDestinyPieceInOpponentList) {
             getOpponentPlayer(pieceOwner).addPieces(originalDestinyPiece);
        }

        return isInCheck;
    }
        
    public static boolean tentarRoque(King king, int targetKingRow, int targetKingCol) throws Exception {
        if (king.hasMoved() || isKingInCheck(king.getColor())) {
            throw new Exception("Roque não permitido: rei já moveu ou está em xeque.");
        }

        int oldKingCol = king.getPosY();
        if (targetKingRow != king.getPosX() || Math.abs(targetKingCol - oldKingCol) != 2) {
            throw new Exception("Movimento de roque inválido para o rei.");
        }

        int rookCol = (targetKingCol > oldKingCol) ? 7 : 0;
        Piece rook = Board.getPiece(king.getPosX(), rookCol);

        if (!(rook instanceof Tower) || rook.hasMoved()) {
            throw new Exception("Roque não permitido: torre inválida ou já moveu.");
        }

        int step = (targetKingCol > oldKingCol) ? 1 : -1;
        for (int c = oldKingCol + step; c != rookCol; c += step) {
            if (Board.getPiece(king.getPosX(), c) != null) {
                throw new Exception("Roque não permitido: caminho não está livre.");
            }
        }

        if (moveLeavesKingInCheck(king, king.getPosX(), oldKingCol, king.getPosX(), oldKingCol + step)) {
            throw new Exception("Roque não permitido: rei passaria por xeque.");
        }
        
        if (moveLeavesKingInCheck(king, king.getPosX(), oldKingCol, king.getPosX(), targetKingCol)) {
             throw new Exception("Roque não permitido: rei terminaria em xeque.");
        }
        
        Board.setPiece(king.getPosX(), oldKingCol, null);
        Board.setPiece(rook.getPosX(), rookCol, null);

        king.setPosY(targetKingCol);
        king.setHasMoved(true);
        Board.setPiece(king.getPosX(), king.getPosY(), king);

        rook.setPosY(targetKingCol - step);
        rook.setHasMoved(true);
        Board.setPiece(rook.getPosX(), rook.getPosY(), rook);
        
        displayMessage(king.getColor() + " realizou o roque!");
        return true;
    }

    public static boolean isGameOver() {
        if (actualPlayer == null) return true;
        return isCheckmate(actualPlayer) || isStalemate(actualPlayer);
    }
    
    private static boolean hasLegalMoves(Player player) {
        for (Piece piece : player.getPieces()) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    try {
                        
                        int oldX = piece.getPosX();
                        int oldY = piece.getPosY();
                        boolean oldMoved = piece.hasMoved();
                        Point oldLastPos = piece.getLastPosition();
                        Piece targetPiece = Board.getPiece(r,c);

                        if (!moveLeavesKingInCheck(piece, oldX, oldY, r, c)) {
                    
                            return true;
                        }
                        
                         piece.setPosX(oldX);
                         piece.setPosY(oldY);
                         piece.setHasMoved(oldMoved);
                         piece.setLastPosition(oldLastPos);
                         Board.setPiece(r,c, targetPiece);
                         Board.setPiece(oldX,oldY, piece);

                    } catch (Exception e) {
                        
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCheckmate(Player playerInCheck) {
        return isKingInCheck(playerInCheck.getColor()) && !hasLegalMoves(playerInCheck);
    }

    public static boolean isStalemate(Player playerWhoseTurnItIs) {
        return !isKingInCheck(playerWhoseTurnItIs.getColor()) && !hasLegalMoves(playerWhoseTurnItIs);
    }
    
    public static void printGame() {
        Board.printBoard();
        if (actualPlayer != null) {
            System.out.println("\n--- Turno de: " + actualPlayer.getName() + " (" + actualPlayer.getColor() + ") ---");
            System.out.println("Mão de Cartas:");
            List<AbstractCard> hand = actualPlayer.getHand();
            if (hand.isEmpty()) {
                System.out.println("  (Nenhuma carta na mão)");
            } else {
                for (int i = 0; i < hand.size(); i++) {
                    AbstractCard card = hand.get(i);
                    System.out.println("  " + (i + 1) + ". " + card.toString() + " (" + card.getCooldownStatus(actualPlayer) + ")");
                }
            }
        } else {
            System.out.println("\nJogo não iniciado ou finalizado.");
        }
    }

    public static Player getWhitePlayer() { return whitePlayer; }
    public static Player getBlackPlayer() { return blackPlayer; }
    public static Player getActualPlayer() { return actualPlayer; }

    public static void printHelp() {
        System.out.println("\nComandos disponiveis:");
        System.out.println("  move <origem> <destino> - Move uma peca (ex: move a2 a4)");
        System.out.println("  use <numero da carta>   - Usa a carta da sua mao com o número especificado");
        System.out.println("  hand                    - Mostra sua mão de cartas e status (já exibido a cada turno)");
        System.out.println("  help                    - Mostra esta ajuda");
        System.out.println("  exit                    - Sai do jogo");
    }

    public static void displayMessage(String message) {
        System.out.println("==> " + message);
    }

    public static Optional<Point> requestCoordinateInput(String promptMessage) {
        displayMessage(promptMessage);
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.matches("[a-h][1-8]")) {
            int col = input.charAt(0) - 'a';
            int row = 8 - Character.getNumericValue(input.charAt(1));
            return Optional.of(new Point(row, col));
        }
        displayMessage("Coordenada inválida. Formato: a1, h8, etc.");
        return Optional.empty();
    }

    public static Optional<Piece> requestPieceSelectionFromPlayer(Player player, String promptMessage, Predicate<Piece> pieceFilter) {
        while (true) {
            displayMessage(promptMessage + " (Ex: a2). Digite 'cancelar' para voltar.");
            String inputStr = scanner.nextLine().trim().toLowerCase();
            if (inputStr.equals("cancelar")) return Optional.empty();

            if (inputStr.matches("[a-h][1-8]")) {
                int col = inputStr.charAt(0) - 'a';
                int row = 8 - Character.getNumericValue(inputStr.charAt(1));
                Point coord = new Point(row, col);

                Piece selected = Board.getPieceFromBoard(coord.x, coord.y);
                if (selected != null && selected.getColor() == player.getColor() && pieceFilter.test(selected)) {
                    return Optional.of(selected);
                } else {
                    displayMessage("Peça inválida, não pertence a você, ou não satisfaz o critério. Tente novamente.");
                }
            } else {
                displayMessage("Formato de coordenada inválido. Use a1, h8, etc. Tente novamente.");
            }
        }
    }
    
    public static Optional<Piece> requestPieceSelectionFromBoard(String promptMessage, Predicate<Piece> pieceFilter) {
         while (true) {
            displayMessage(promptMessage + " (Ex: e4). Digite 'cancelar' para voltar.");
            String inputStr = scanner.nextLine().trim().toLowerCase();
            if (inputStr.equals("cancelar")) return Optional.empty();

            if (inputStr.matches("[a-h][1-8]")) {
                int col = inputStr.charAt(0) - 'a';
                int row = 8 - Character.getNumericValue(inputStr.charAt(1));
                Point coord = new Point(row, col);

                Piece selected = Board.getPieceFromBoard(coord.x, coord.y);
                if (selected != null && pieceFilter.test(selected)) {
                    return Optional.of(selected);
                } else {
                    displayMessage("Nenhuma peça válida na coordenada ou não satisfaz o critério. Tente novamente.");
                }
            } else {
                displayMessage("Formato de coordenada inválido. Use a1, h8, etc. Tente novamente.");
            }
        }
    }

    public static Optional<PieceType> requestPieceTypeSelection(Player player, String promptMessage, List<PieceType> availableTypes) {
        if (availableTypes.isEmpty()) {
            displayMessage("Nenhum tipo de peça disponível para seleção.");
            return Optional.empty();
        }
        while (true) {
            displayMessage(promptMessage + " Tipos disponíveis: " + 
                           availableTypes.stream().map(Enum::name).collect(Collectors.joining(", ")) + 
                           ". Digite 'cancelar' para voltar.");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equalsIgnoreCase("cancelar")) return Optional.empty();

            try {
                PieceType selectedType = PieceType.valueOf(input);
                if (availableTypes.contains(selectedType)) {
                    return Optional.of(selectedType);
                } else {
                    displayMessage("Tipo de peça indisponível ou inválido. Tente novamente.");
                }
            } catch (IllegalArgumentException e) {
                displayMessage("Tipo de peça inválido. Tente novamente.");
            }
        }
    }
    
    public static Player getOpponentPlayer(Player player) {
        if (player == null) return null;
        return (player == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public static void removePieceFromGame(Piece piece) {
        if (piece == null) return;
        Player owner = (piece.getColor() == Color.WHITE) ? whitePlayer : blackPlayer;
        if (owner != null) {
            owner.removePiece(piece);
        }
        Board.setPiece(piece.getPosX(), piece.getPosY(), null);
        displayMessage("Peça " + piece.getType() + " em " + coordsToAlgebraic(piece.getPosX(), piece.getPosY()) + " removida do jogo.");
        Board.updateBoard();
    }
    
    public static boolean isValidBoardPosition(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public static String coordsToAlgebraic(int r, int c) {
        if (!isValidBoardPosition(r,c)) return "INVALID";
        char colChar = (char) ('a' + c);
        char rowChar = (char) ('8' - r);
        return "" + colChar + rowChar;
    }

    public static void drawNewCardForPlayer(Player player, int numberOfCards) {
        if (player == null) return;
        for (int i = 0; i < numberOfCards; i++) {
            AbstractCard drawnCard = drawRandomCard();
            if (drawnCard != null) {
                player.addCardToHand(drawnCard);
                displayMessage(player.getName() + " sacou a carta: " + drawnCard.getName());
            } else {
                displayMessage("Não foi possível sacar uma nova carta (baralho de protótipos vazio ou erro).");
            }
        }
    }

    public static AbstractCard drawRandomCard() {
        if (AVAILABLE_CARD_SUPPLIERS.isEmpty()) return null;
        Random rand = new Random();
        
        double prob = rand.nextDouble();
        RarityType targetRarity;
        if (prob < 0.63) targetRarity = RarityType.COMUM;
        else if (prob < 0.93) targetRarity = RarityType.RARA;
        else targetRarity = RarityType.LENDARIA;

        List<Supplier<AbstractCard>> candidates = AVAILABLE_CARD_SUPPLIERS.stream()
            .map(supplier -> {
                try { return supplier.get(); } catch (Exception e) { return null; }
            })
            .filter(java.util.Objects::nonNull)
            .filter(card -> card.getRarityType() == targetRarity)
            .map(card -> (Supplier<AbstractCard>) () -> {
                try { return card.getClass().getDeclaredConstructor().newInstance(); } 
                catch (Exception e) { e.printStackTrace(); return null; }
            })
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toList());

        Supplier<AbstractCard> chosenSupplier;
        if (candidates.isEmpty()) {
            chosenSupplier = AVAILABLE_CARD_SUPPLIERS.get(rand.nextInt(AVAILABLE_CARD_SUPPLIERS.size()));
        } else {
            chosenSupplier = candidates.get(rand.nextInt(candidates.size()));
        }
        try {
            return chosenSupplier.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static void playerUsesCardCommand(int cardIndexInHand) {
        if (actualPlayer == null) return;
        if (actualPlayer.hasUsedCardThisTurn()) {
            displayMessage("Você já usou uma carta neste turno!");
            return;
        }
        if (cardIndexInHand < 1 || cardIndexInHand > actualPlayer.getHand().size()) {
            displayMessage("Número da carta inválido. Verifique sua mão.");
            return;
        }
        AbstractCard cardToUse = actualPlayer.getHand().get(cardIndexInHand - 1);

        boolean success = cardToUse.activate(actualPlayer);

        if (success) {
            actualPlayer.removeCardFromHand(cardToUse);
            actualPlayer.addToDiscardPile(cardToUse); // Move para o descarte
            actualPlayer.setUsedCardThisTurn(true);   // Marca que já usou carta neste turno
        }
    }
    
    
    public static void processEndOfTurn(Player playerWhoseTurnEnded) {
        if(playerWhoseTurnEnded == null) return;

        playerWhoseTurnEnded.decrementCooldownsAndEffects();

        for (Piece piece : playerWhoseTurnEnded.getPieces()) {
            if (piece.getTurnsBlocked() > 0) {
                piece.decrementTurnsBlocked();
                if(piece.getTurnsBlocked() == 0) displayMessage("Peça " + piece.getType() + " de " + playerWhoseTurnEnded.getName() + " não está mais bloqueada.");
            }
            if (piece.getType() == PieceType.KNIGHT && piece.isAffectedBySlipperySolo()) {
                 piece.clearSlipperySoloEffect();
                 displayMessage("Efeito Solo Escorregadio removido do cavalo de " + playerWhoseTurnEnded.getName() + ".");
            }
        }
        
        Board.decrementActiveBoardEffectsTimers();
        
    }

    public static boolean attemptSpecialMove(Piece piece, int targetX, int targetY, boolean allowCapture) throws Exception {
        if (piece == null) throw new IllegalArgumentException("Peça não pode ser nula.");
        if (!isValidBoardPosition(targetX, targetY)) throw new Exception("Posição de destino inválida.");

        Point oldPos = new Point(piece.getPosX(), piece.getPosY());
        Piece pieceAtTarget = Board.getPieceFromBoard(targetX, targetY);

        if (!allowCapture && pieceAtTarget != null) {
            throw new Exception("Movimento especial não permite captura e o destino está ocupado.");
        }
        if (allowCapture && pieceAtTarget != null && pieceAtTarget.getColor() == piece.getColor()) {
            throw new Exception("Não pode capturar sua própria peça.");
        }

        if (!allowCapture && pieceAtTarget != null) { 
             throw new Exception("Movimento não permite captura e o destino está ocupado.");
        }

        if (moveLeavesKingInCheck(piece, oldPos.x, oldPos.y, targetX, targetY)) {
            throw new Exception("Movimento especial resultaria em xeque para seu próprio rei.");
        }
         
        piece.setPosX(targetX);
        piece.setPosY(targetY);
        piece.setLastPosition(oldPos);
        if (!(piece instanceof King && Math.abs(targetY - oldPos.y) == 2)) { 
            piece.setHasMoved(true);
        }


        Board.setPiece(oldPos.x, oldPos.y, null);
        Board.setPiece(targetX, targetY, piece);

        if (allowCapture && pieceAtTarget != null) { 
            Player opponent = getOpponentPlayer(actualPlayer); 
            opponent.removePiece(pieceAtTarget);
            opponent.addCapturedOwnPiece(pieceAtTarget);
            displayMessage(actualPlayer.getName() + " capturou " + pieceAtTarget.getType() + " de " + opponent.getName() + " com movimento especial.");

        }
        Board.updateBoard();
        return true;
    }

    public static boolean revivePieceForPlayer(Player player, PieceType typeToRevive, int targetX, int targetY) {
        if (player == null || typeToRevive == null || !isValidBoardPosition(targetX, targetY)) return false;
        if (!Board.isPositionEmpty(targetX, targetY)) {
            displayMessage("Casa de renascimento (" + coordsToAlgebraic(targetX, targetY) + ") não está vazia.");
            return false;
        }

        boolean removedFromCaptured = player.removeRevivedPieceFromCapturedList(typeToRevive);
        if (!removedFromCaptured) {
            displayMessage("Peça do tipo " + typeToRevive + " não encontrada na lista de capturadas de " + player.getName() + ".");
            return false;
        }

        Piece revivedPiece = createNewPiece(typeToRevive, player.getColor(), targetX, targetY);
        if (revivedPiece == null) {
            displayMessage("Erro ao criar nova instância da peça " + typeToRevive + ".");

            return false; 
        }
        revivedPiece.setHasMoved(true); 

        player.addPieces(revivedPiece); 
        Board.setPiece(targetX, targetY, revivedPiece); 
        Board.updateBoard();
        return true;
    }


    private static Piece createNewPiece(PieceType type, Color color, int x, int y) {
        switch (type) {
            case PAWN: return new Pawn(x, y, color, type);
            case TOWER: return new Tower(x, y, color, type);
            case KNIGHT: return new Knight(x, y, color, type);
            case BISP: return new Bisp(x, y, color, type);
            case QUEEN: return new Queen(x, y, color, type);
            case KING: return new King(x, y, color, type); 
            default: return null;
        }
    }

public static Scanner getScanner() { return scanner; }

}

