package models.pieces;

import models.Board;
import models.enums.Color;
import models.enums.PieceType;

public class Queen extends Piece {

    public Queen(int posX, int posY, Color color, PieceType pieceSurname) {
        super(posX, posY, color, pieceSurname);
    }

    @Override
    public void movement(int newX, int newY, Piece destinyPlace) {
        int currentX = this.getPosX();
        int currentY = this.getPosY();
        int dx = newX - currentX;
        int dy = newY - currentY;

        // Determinar o tipo de movimento válido
        boolean diagonal = Math.abs(dx) == Math.abs(dy);
        boolean horizontal = dx == 0 && dy != 0;
        boolean vertical = dx != 0 && dy == 0;

        if (!(diagonal || horizontal || vertical)) {
            throw new IllegalArgumentException("Movimento inválido para a Rainha.");
        }

        int stepX = Integer.compare(dx, 0); // -1, 0 ou 1
        int stepY = Integer.compare(dy, 0);

        int x = currentX + stepX;
        int y = currentY + stepY;

        // Verifica obstáculos no caminho (ignora destino por enquanto)
        while (x != newX || y != newY) {
            if (Board.getPieceFromBoard(x, y) != null) {
                throw new IllegalStateException("Caminho bloqueado por peça em: (" + x + ", " + y + ")");
            }
            x += stepX;
            y += stepY;
        }

        // Verifica destino
        Piece target = Board.getPieceFromBoard(newX, newY);
        if (target == null || target.getColor() != this.getColor()) {
            this.setPosX(newX);
            this.setPosY(newY);
        } else {
            throw new IllegalStateException("Não pode capturar uma peça da mesma cor.");
        }
    }
}
