import java.util.Scanner;

import models.GameLogic;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String input;

        GameLogic.startGame();

        do {
            GameLogic.printGame();

            input = sc.nextLine();

        } while(input != "exit");

        sc.close();
    }
}