import java.util.*;
import java.lang.*;

public class Deck {
    ArrayList<Card> cards;

    Deck() {
        cards = FillDeck();
    }

    // Initializes 52 card deck
    private ArrayList<Card> FillDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                Card card = new Card(j, Suit.getSuit(i));
                deck.add(card);
            }
        }
        return deck;
    }

    // Removes a random card from the deck and returns it
    public Card Draw() {
        Random rand = new Random();
        int randVal = rand.nextInt(cards.size());
        Card card = cards.remove(randVal);
        return card;
    }

    // Draws amount cards and adds them to hand
    public ArrayList<Card> Deal(Hand hand, int amount) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < amount; i++) {
            cards.add(Draw());
        }
        hand.Fill(cards);
        hand.Sort();
        return cards;
    }

    // adds all cards in hand back into the deck
    public void Reshuffle(Hand hand) {
        cards.addAll(hand.getCards());
        hand.Empty();
    }
}