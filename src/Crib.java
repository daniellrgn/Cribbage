import java.util.*;

public class Crib extends Hand {

    public Crib(){
        super();
    }

    public Crib(ArrayList<Card> cards) {
        super(cards);
    }
    
    @Override
    public int Score(Card upcard) {
        cards.add(upcard);
        int total = super.Score();
        cards.remove(upcard);
        total += super.Nobs(upcard) + Flush(upcard);
        return total;
    }

    private int Flush(Card upcard) {
        flush.clear();
        Suit suit = upcard.suit;
        for (Card c : cards) {
            if (c.suit != suit) {
                flush.clear();
                return 0;
            }
            flush.add(c);
        }
        return flush.size();
    }
}