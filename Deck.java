import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a standard 108-card Uno deck with built-in shuffling,
 * state tracking, and draw mechanics.
 */
public class Deck {
    private ArrayList<Card> deck = new ArrayList<>();
    private int currentIndex = 0;

    public Deck() {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};

        for (String color : colors) {
            deck.add(new Card("0", color, false));

            for (int i = 1; i <= 9; i++) {
                String face = String.valueOf(i);
                deck.add(new Card(face, color, false));
                deck.add(new Card(face, color, false));
            }

            String[] actions = {"Skip", "Reverse", "Draw Two"};
            for (String action : actions) {
                deck.add(new Card(action, color, false));
                deck.add(new Card(action, color, false));
            }
        }

        for (int i = 0; i < 4; i++) {
            deck.add(new Card("Wild", "Black", true));
            deck.add(new Card("Draw Four", "Black", true));
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(deck);
        currentIndex = 0;
    }

    public Card draw() {
        if (currentIndex >= deck.size()) {
            shuffle();
        }
        Card drawnCard = deck.get(currentIndex);
        currentIndex++;
        return drawnCard;
    }

    /**
     * Returns a visually formatted summary of the deck's current state.
     */
    @Override
    public String toString() {
        int remainingCards = deck.size() - currentIndex;
        return "╔══════════════════════════╗\n" +
                "║        UNO DECK          ║\n" +
                "╠══════════════════════════╣\n" +
                "║ Total Cards: " + deck.size() + "          ║\n" +
                "║ Remaining:   " + remainingCards + "          ║\n" +
                "║ Drawn Index: " + currentIndex + "          ║\n" +
                "╚══════════════════════════╝";
    }
}
