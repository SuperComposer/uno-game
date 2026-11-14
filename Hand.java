import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a player's hand in an Uno game, supporting card management,
 * sorting, and play validation.
 */
public class Hand {
    private ArrayList<Card> hand = new ArrayList<>();

    public Hand(Deck deck) {
        draw(deck, 7);
    }

    public void draw(Deck deck, int count) {
        for (int i = 0; i < count; i++) {
            hand.add(deck.draw());
        }
    }

    public boolean play(Card c) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).equals(c)) {
                hand.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void orderBy(boolean byFace) {
        if (byFace) {
            hand.sort(Comparator.comparing(Card::getFace));
        } else {
            hand.sort(Comparator.comparing(Card::getColor));
        }
    }

    public int size() {
        return hand.size();
    }

    public boolean hasPlayableCard(Card topCard) {
        for (Card c : hand) {
            if (c.canPlay(topCard)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a neat, numbered list representation of all cards currently held in the player's hand.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Player Hand (").append(hand.size()).append(" cards) ===\n");
        for (int i = 0; i < hand.size(); i++) {
            sb.append(String.format("[%3d]: %s\n", i + 1, "\n" + hand.get(i).toString()));

        }
        sb.append("===========================");
        return sb.toString();
    }
}
