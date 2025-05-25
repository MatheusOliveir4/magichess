package models.enums;

public enum PieceType {
    PAWN("P", "p"), TOWER("T", "t"), KNIGHT("C", "c"),
    BISP("B", "b"), QUEEN("D", "d"), KING("R", "r");

    private final String whiteSymbol, blackSymbol;
    
    PieceType(String ws, String bs) { 
        whiteSymbol = ws; blackSymbol = bs;
    }

    public String getSymbol(Color color) {
        return color == Color.WHITE ? whiteSymbol : blackSymbol;
    }
}
