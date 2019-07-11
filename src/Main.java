public class Main {

    public static void main(String[] args) {
        Deck deck = new Deck();
        Hand hand1 = new Hand();
        deck.Deal(hand1, 6);
        hand1.DisplayCards();
        hand1.Score();
        hand1.DisplayScore();
    }
}