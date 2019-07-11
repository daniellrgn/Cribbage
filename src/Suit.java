public enum Suit {
    S,
    H,
    D,
    C;

    private static Suit[] list = Suit.values();

    public static Suit getSuit(int i) {
        return list[i];
    }
}