import java.util.Scanner;

import models.Board;
import models.GameLogic;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String input;

        GameLogic.startGame();

        do {
            try {
                GameLogic.printGame();
                
              

                System.out.print("\nDigite seu comando ('help', 'move (Peca de Origem) (Destino)', 'use (Numero da Carta)', 'pass'): ");
                if (!sc.hasNextLine()) {
                    System.out.println("Entrada encerrada. Jogo finalizado.");
                    break;
                }

                input = sc.nextLine().trim().toLowerCase();

                if (input.equals("exit")) { 
                    break;
                
                } else if (input.equals("help")) {
                    GameLogic.printHelp();
                
                } else if (input.startsWith("move ")) {
                    String[] parts = input.split(" ");

                    if (parts.length == 3) {
                        int fromX = parts[1].charAt(0) - 'a';
                        int fromY = 8 - Character.getNumericValue(parts[1].charAt(1));
                        int toX = parts[2].charAt(0) - 'a';
                        int toY = 8 - Character.getNumericValue(parts[2].charAt(1));
                    
                        GameLogic.movePiece(fromX, fromY, toX, toY);

                    } else {
                        System.out.println("Movimento invalido ou falhou!");
                    } 
                
                } else {
                    System.out.println("Formato: 'move <origem> <destino>' (ex: move a2 a4).");
                } 

            } catch (Exception e) {

            }

        } while (true);

        sc.close();
    }
}