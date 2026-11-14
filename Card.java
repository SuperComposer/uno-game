/**
 * Represents a single Uno card, containing a face value, color, and wild status,
 * along with game logic for match validity.
 */
public class Card {
    private String face;
    private String color;
    private boolean wild;

    public Card(String face, String color, boolean wild){
        this.face = face;
        this.color = color;
        this.wild = wild;
    }

    public String getFace(){
        return face;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    public boolean getWild(){
        return wild;
    }

    public boolean canPlay(Card c){
        if(c.getFace().equals(face) || c.getColor().equals(color)){
            return true;
        }else if(wild){
            return true;
        }
        return false;
    }

    public boolean canPlay(String color){
        if(this.color == color || wild){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card){
            Card c = (Card) obj;
            if(c.getFace() == face && c.getColor() == color && c.getWild() == wild){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a visually appealing string representation of the card,
     * styling wild cards distinctively from colored cards.
     */
    @Override
    public String toString() {
        if (wild) {
            return "🌟[ " + face + " ]🌟";
        }
        return "┌─────────┐\n" +
                "│ " + color + " ".repeat(Math.max(0, 8 - color.length())) + "│\n" +
                "│    " + face + "    │\n" +
                "└─────────┘";
    }
}
