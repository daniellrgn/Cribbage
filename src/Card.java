import java.util.*;
import java.lang.*;

public class Card {
    public int value;
    public int rank;
    public String symbol;
    public Suit suit;

    Card() {
        value = 0;
        rank = 0;
        symbol = "";
        suit = null;
    }

    Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        switch (rank) {
            case 1:
                symbol = "A";
                break;
            case 11:
                symbol = "J";
                break;
            case 12:
                symbol = "Q";
                break;
            case 13:
                symbol = "K";
                break;
            default:
                symbol = Integer.toString(rank);
                break;
        }
        value = rank < 10 ? rank : 10;
    }

    Card(int rank, String symbol, Suit suit) {
        this.rank = rank;
        this.symbol = symbol;
        this.suit = suit;
        this.value = rank < 10 ? rank : 10;
    }

    Card(int value, int rank, String symbol, Suit suit) {
        this.value = value;
        this.rank = rank;
        this.symbol = symbol;
        this.suit = suit;
    }
}