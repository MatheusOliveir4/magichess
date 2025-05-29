import models.GameLogic; 

public class Main {
    public static void main(String[] args) {
        String input;

        GameLogic.startGame();

        do {
            try {
                GameLogic.printGame(); 
                
                if (GameLogic.getActualPlayer() == null) { 
                    System.out.println("Fim de jogo detectado em Main.");
                    break;
                }

                System.out.print("\n" + GameLogic.getActualPlayer().getName() + 
                                   ", digite seu comando ('help', 'move O D', 'use N', '', 'exit'): ");
                if (!GameLogic.getScanner().hasNextLine()) { 
                    System.out.println("Entrada encerrada. Jogo finalizado.");
                    break; 
                }
                input = GameLogic.getScanner().nextLine().trim().toLowerCase();

                if (input.equals("exit")) { 
                    break;
                } else if (input.equals("help")) {
                    GameLogic.printHelp();
                } else if (input.startsWith("move ")) {
                    String[] parts = input.split(" ");
                    if (parts.length == 3) {
                        if (parts[1].matches("[a-h][1-8]") && parts[2].matches("[a-h][1-8]")) {
                            int fromCol = parts[1].charAt(0) - 'a';
                            int fromRow = 8 - Character.getNumericValue(parts[1].charAt(1));
                            int toCol = parts[2].charAt(0) - 'a';
                            int toRow = 8 - Character.getNumericValue(parts[2].charAt(1));
                            
                            GameLogic.movePiece(fromRow, fromCol, toRow, toCol);
                        } else {
                            System.out.println("Formato de coordenada inválido. Use duas letras (ex: a1 h8).");
                        }
                    } else {
                        System.out.println("Formato: 'move <origem> <destino>' (ex: move a2 a4).");
                    }
                } else if (input.startsWith("use ")) {
                    String[] parts = input.split(" ");
                    if (parts.length == 2) {
                        try {
                            int cardNumber = Integer.parseInt(parts[1]);
                            GameLogic.playerUsesCardCommand(cardNumber);
                        } catch (NumberFormatException e) {
                            System.out.println("Número da carta inválido. Use 'use <numero>'.");
                        }
                    } else {
                        System.out.println("Formato: 'use <numero da carta>'.");
                    }
                } else if (input.isEmpty()) {

                }
                 else {
                    System.out.println("Comando desconhecido: '" + input + "'. Digite 'help' para ajuda.");
                }

            } catch (Exception e) {
                System.out.println("!! Erro no Jogo: " + e.getMessage() + " !!");
            }

        } while (!GameLogic.isGameOver());

        System.out.println("\n----- JOGO FINALIZADO -----");
    }
}