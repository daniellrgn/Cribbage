import java.util.*;
import java.lang.*;

/* Public Methods:
 * int Score()
 * int Score(Card upcard)
 * void DisplayScore()
 * void Empty()
 * void Add(Card card)
 * void Remove(Card card)
 * int compare(Card a, Card b)
 */

public class Hand {
    public ArrayList<Card> cards;               //cards in hand
    public ArrayList<ArrayList<Card>> fifteens;             //sets of unique cards adding to 15
    public ArrayList<ArrayList<Card>> pairs;                //unique pairs
    public ArrayList<ArrayList<Card>> runs;             //sets of unique cards in runs
    public ArrayList<Card> flush;               //cards involved in flush
    public ArrayList<Card> nobs;                //card involved in nobs

    private int fifteenScore;
    private int pairScore;
    private int runScore;

    public Hand() {
        cards = new ArrayList<Card>();
        fifteens = new ArrayList<ArrayList<Card>>();
        pairs = new ArrayList<ArrayList<Card>>();
        runs = new ArrayList<ArrayList<Card>>();
        flush = new ArrayList<Card>();
        nobs = new ArrayList<Card>();
        fifteenScore = 0;
        pairScore = 0;
        runScore = 0;
    }

    public Hand(ArrayList<Card> hand) {
        cards = hand;
        fifteens = new ArrayList<ArrayList<Card>>();
        pairs = new ArrayList<ArrayList<Card>>();
        runs = new ArrayList<ArrayList<Card>>();
        flush = new ArrayList<Card>();
        nobs = new ArrayList<Card>();
        fifteenScore = 0;
        pairScore = 0;
        runScore = 0;
    }

    public int Score() {
        Sort();
        fifteenScore = Fifteens();
        pairScore = Pairs();
        runScore = Runs();
        int total = fifteenScore + pairScore + runScore;
        return total;
    }

    public int Score(Card upcard) {
        cards.add(upcard);
        int total = Score();
        cards.remove(upcard);
        total += Nobs(upcard) + Flush(upcard);
        return total;
    }

    // returns total score of all fifteens, and list of combinations is filled
    private int Fifteens() {
        fifteens.clear(); //= new ArrayList<ArrayList<Card>>();
        ArrayList<Card> included = new ArrayList<Card>();
        ArrayList<Card> notIncluded = new ArrayList<Card>(cards);
        MakeFifteens(0, included, notIncluded, 0);
        return fifteens.size() * 2;
    }

    // adds combinations of cards to fifteens list
    private void MakeFifteens(int currentSum, ArrayList<Card> included, 
                                ArrayList<Card> notIncluded, int startIndex) {
                                    
        for (int index = startIndex; index < notIncluded.size(); index++) {
            Card nextCard = notIncluded.get(index);
            if (currentSum + nextCard.value == 15) {
                ArrayList<Card> newResult = new ArrayList<Card>(included);
                newResult.add(nextCard);
                fifteens.add(newResult);
            } else if (currentSum + nextCard.value < 15) {
                ArrayList<Card> nextIncluded = new ArrayList<Card>(included);
                nextIncluded.add(nextCard);
                ArrayList<Card> nextNotIncluded = new ArrayList<Card>(notIncluded);
                nextNotIncluded.remove(nextCard);
                MakeFifteens(currentSum + nextCard.value, nextIncluded, nextNotIncluded, startIndex++);
            }
        }
    }

    // adds pairs to list and returns total pair score
    private int Pairs() {
        pairs.clear();
        for (int i = 0; i < cards.size()-1; i++) {
            Card cardA = cards.get(i);
            for (int j = i+1; j < cards.size(); j++) {
                Card cardB = cards.get(j);
                if (cardA.rank == cardB.rank) {
                    ArrayList<Card> pair = new ArrayList<Card>();
                    pair.add(cardA);
                    pair.add(cardB);
                    pairs.add(pair);
                }
            }
        }
        return pairs.size() * 2;
    }

    // Assumes list is sorted low to high
    // Obsolete!
    // private int SimpleRuns() {
    //     int total = 0;
    //     int runValue = 0;
    //     int cardCount = 0;
    //     ArrayList<Integer> cardCounts = new ArrayList<Integer>();

    //     Card prev = null;
    //     for (Card next : cards) {
    //         if (prev != null) {
    //             if (prev.rank == next.rank) {
    //                 cardCount++;
    //             } else {
    //                 if (next.rank == prev.rank+1) {
    //                     runValue++;
    //                 } else {
    //                     for (Integer count : cardCounts) {
    //                         runValue *= count;
    //                     }
    //                     total += runValue;
    //                     runValue = 0;
    //                     cardCounts.clear();
    //                 }
    //                 cardCounts.add(cardCount);
    //                 cardCount = 0;
    //             }
    //         }
    //         prev = next;
    //     }
    //     return total;
    // }


    private int Runs() {
        int total = 0;
        ArrayList<ArrayList<Card>> run = new ArrayList<ArrayList<Card>>();
        ArrayList<Card> copies = new ArrayList<Card>();

        runs.clear();

        Card last = null;
        for (Card next : cards) {
            if (last != null) {
                if (next.rank == last.rank) {
                    copies.add(next);
                } else { // next is either the next in the run or the run is complete.
                    
                    // Add runs with the new card replaced by its copies to run.
                    ArrayList<ArrayList<Card>> runCopies = new ArrayList<ArrayList<Card>>();
                    for (Card c : copies) {
                        runCopies.addAll(ChangeLast((ArrayList<ArrayList<Card>>)run.clone(), c));
                    }
                    run.addAll(runCopies);
                    copies.clear();

                    if (next.rank == last.rank+1) {
                        run = AddToAll(run, next);
                    } else {
                        if (run.size() > 0 && run.get(0).size() > 2) {
                            runs.addAll(run);
                        }
                    }
                }
            }
            last = next;
        }
        return total;
    }

    // adds a card to all sublists in an arraylist
    private ArrayList<ArrayList<Card>> AddToAll(ArrayList<ArrayList<Card>> lists, Card card) {
        for (ArrayList<Card> list : lists) {
            list.add(card);
        }
        return lists;
    }

    // sets the last card in each sublist to the specified card
    private ArrayList<ArrayList<Card>> ChangeLast(ArrayList<ArrayList<Card>> lists, Card card) {
        for (ArrayList<Card> list : lists) {
            list.set(list.size()-1, card);
        }
        return lists;
    }

    protected int Nobs(Card upcard) {
        nobs.clear();
        for (Card card : cards) {
            if (card.value == 11 && card.suit == upcard.suit && !card.equals(upcard)){
                nobs.add(card);
                return 1;
            }
        }
        return 0;
    }

    private int Flush(Card upcard) {
        flush.clear();
        Suit suit = cards.get(0).suit;
        for (Card card : cards) {
            if (card.suit == suit) {
                flush.add(card);
            }
        }
        if (flush.size() >= 4) {
            if (upcard.suit == suit) {
                flush.add(upcard);
            }
            return flush.size();
        } else {
            flush.clear();
            return 0;
        }
    }

    public void DisplayCards() {
        System.out.print("[ ");
        for (Card c : cards) {
            System.out.print(c.symbol+c.suit+" ");
        }
        System.out.println("]");
    }
    
    public void DisplayScore() {
        DisplayMany(fifteens, "fifteen", fifteenScore);
        DisplayMany(pairs, "pair", pairScore);
        DisplayMany(runs, "run", runScore);
        DisplayOne(flush, "Flush");
        DisplayOne(nobs, "Nobs");
    }

    protected void DisplayMany(ArrayList<ArrayList<Card>> list, String name, int score) {
        if (list.size() > 0) {
            name += list.size() > 1 ? "s" : "";
            System.out.println(list.size()+" "+ name +" for "+ score +":");
            for (ArrayList<Card> f : list) {
                System.out.print("[ ");
                for (Card c : f) {
                    System.out.print(c.symbol + c.suit+" ");
                }
                System.out.println("]");
            }
        }
    }

    protected void DisplayOne(ArrayList<Card> list, String name) {
        if (list.size() > 0) {
            System.out.println(name+" for "+list.size()+":");
            System.out.print("[ ");
            for (Card c : list) {
                System.out.print(c.symbol+c.suit+" ");
            }
            System.out.println("]");
        }
    }

    public void Fill(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public void Empty() {
        cards.clear();
        fifteens.clear();
        pairs.clear();
        runs.clear();
        flush.clear();
        nobs.clear();
    }
    
    public void Add(Card card) {
        cards.add(card);
    }

    public void Remove(Card card) {
        cards.remove(card);
    }

    public void Sort() {
        Comparator<Card> c = new Comparator<Card>() {
            @Override
            public int compare(Card a, Card b) {
                if (a.value < b.value) {
                    return -1;
                } else if (b.value < a.value) {
                    return 1;
                } else {
                    if (a.suit.ordinal() < b.suit.ordinal()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        };
        Collections.sort(cards, c);
    }

// ==================== Getters and setters ==========================

    /**
     * @return the cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * @param cards the cards to set
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
	 * @return the fifteens
	 */
	public ArrayList<ArrayList<Card>> getFifteens() {
		return fifteens;
	}

	/**
	 * @param fifteens the fifteens to set
	 */
	public void setFifteens(ArrayList<ArrayList<Card>> fifteens) {
		this.fifteens = fifteens;
	}

    /**
	 * @return the pairs
	 */
	public ArrayList<ArrayList<Card>> getPairs() {
		return pairs;
	}

	/**
	 * @param pairs the pairs to set
	 */
	public void setPairs(ArrayList<ArrayList<Card>> pairs) {
		this.pairs = pairs;
	}

	/**
	 * @return the runs
	 */
	public ArrayList<ArrayList<Card>> getRuns() {
		return runs;
	}

	/**
	 * @param runs the runs to set
	 */
	public void setRuns(ArrayList<ArrayList<Card>> runs) {
		this.runs = runs;
    }
    
    /**
     * @return the flush
     */
    public ArrayList<Card> getFlush() {
        return flush;
    }

    /**
     * @param flush the flush to set
     */
    public void setFlushes(ArrayList<Card> flush) {
        this.flush = flush;
    }

    /**
     * @return the nobs
     */
    public ArrayList<Card> getNobs() {
        return nobs;
    }

    /**
     * @param nobs the nobs to set
     */
    public void setNobs(ArrayList<Card> nobs) {
        this.nobs = nobs;
    }
}