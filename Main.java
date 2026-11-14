import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Main driver class to run a simulated game of Uno in the console with full power card logic.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // 1. Generate a random 4-digit room code
        int roomCode = 1000 + random.nextInt(9000);
        System.out.println("=================================");
        System.out.println("        WELCOME TO UNO!          ");
        System.out.println("   Room Code Generated: " + roomCode);
        System.out.println("=================================\n");

        // 2. Prompt user for room code entry to "join"
        System.out.print("Enter room code to join: ");
        int enteredCode = scanner.nextInt();

        while (enteredCode != roomCode) {
            System.out.print("Incorrect room code. Try again: ");
            enteredCode = scanner.nextInt();
        }
        printWithDelay("\nSuccessfully joined Room " + roomCode + "!\n", 1000);

        // 3. Prompt for number of players (2 to 6 recommended for Uno)
        int numPlayers = 0;
        while (numPlayers < 2 || numPlayers > 6) {
            System.out.print("Enter number of players (2 - 6): ");
            numPlayers = scanner.nextInt();
        }

        // 4. Initialize Deck and Hands
        Deck deck = new Deck();
        ArrayList<Hand> players = new ArrayList<>();

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Hand(deck));
        }

        // Draw the initial discard pile card (avoid starting with a wild/action if possible)
        Card topCard = deck.draw();
        while (topCard.getWild() || !topCard.getFace().matches("[0-9]")) {
            deck.shuffle();
            topCard = deck.draw();
        }

        printWithDelay("\n--- Game Started! ---", 1000);
        printWithDelay("Starting Discard Pile Card:\n" + topCard.toString(), 2000);

        // 5. Game Loop & State Variables
        int currentPlayerIndex = 0;
        boolean clockwise = true; // true = forward, false = backward (Reverse)
        boolean gameRunning = true;

        while (gameRunning) {
            Hand currentHand = players.get(currentPlayerIndex);
            int playerNum = currentPlayerIndex + 1;
            boolean turnFinished = false;

            // Turn loop: repeats if the player makes an invalid move
            while (!turnFinished && gameRunning) {
                System.out.println("\n--------------------------------------------------");
                printWithDelay(">>> PLAYER " + playerNum + "'s TURN <<<", 1000);
                printWithDelay("Current Top Card:\n" + topCard.toString(), 1000);
                printWithDelay(currentHand.toString(), 3000);

                // Check if player has a playable card
                if (currentHand.hasPlayableCard(topCard)) {
                    System.out.print("Enter the index of the card you want to play (1 to " + currentHand.size() + "), or 0 to draw: ");
                    int choice = scanner.nextInt();

                    if (choice == 0) {
                        printWithDelay("You chose to draw a card.", 1000);
                        currentHand.draw(deck, 1);
                        turnFinished = true;
                    } else if (choice > 0 && choice <= currentHand.size()) {
                        Card chosenCard = currentHand.getHand().get(choice - 1);

                        if (chosenCard.canPlay(topCard)) {
                            // If it's a wild card, prompt for a new color
                            if (chosenCard.getWild()) {
                                scanner.nextLine(); // Consume newline left-over
                                boolean validColorChosen = false;
                                while (!validColorChosen) {
                                    System.out.print("Wild card played! Choose a color (Red, Blue, Green, Yellow): ");
                                    String chosenColor = scanner.nextLine().trim();

                                    if (chosenColor.equalsIgnoreCase("red") ||
                                            chosenColor.equalsIgnoreCase("blue") ||
                                            chosenColor.equalsIgnoreCase("green") ||
                                            chosenColor.equalsIgnoreCase("yellow")) {

                                        String formattedColor = chosenColor.substring(0, 1).toUpperCase() + chosenColor.substring(1).toLowerCase();
                                        chosenCard = new Card(chosenCard.getFace(), formattedColor, true);
                                        validColorChosen = true;
                                    } else {
                                        printWithDelay("❌ Invalid color. Please choose Red, Blue, Green, or Yellow.", 500);
                                    }
                                }
                            }

                            currentHand.play(chosenCard); // Removes the original from hand
                            topCard = chosenCard;
                            printWithDelay("Player " + playerNum + " played:\n" + chosenCard.toString(), 1000);
                            // Check for UNO condition
                            if (currentHand.size() == 1){
                                printWithDelay("Player " + playerNum + " yelled UNO!", 1000);
                            }
                            // Check for win condition
                            if (currentHand.size() == 0) {
                                System.out.println("\n🎉 CONGRATULATIONS! Player " + playerNum + " wins the game! 🎉");
                                gameRunning = false;
                            } else {
                                // --- POWER CARD EFFECTS ---
                                String face = chosenCard.getFace();

                                if (face.equals("Reverse")) {
                                    if (numPlayers == 2) {
                                        currentPlayerIndex = getNextPlayerIndex(currentPlayerIndex, numPlayers, clockwise);
                                        printWithDelay("🔄 Reverse played in 2-player! You get another turn!", 1000);
                                    } else {
                                        clockwise = !clockwise;
                                        printWithDelay("🔄 Direction reversed!", 1000);
                                    }
                                } else if (face.equals("Skip")) {
                                    currentPlayerIndex = getNextPlayerIndex(currentPlayerIndex, numPlayers, clockwise);
                                    printWithDelay("🚫 Player " + (currentPlayerIndex + 1) + " was skipped!", 1000);
                                } else if (face.equals("Draw Two")) {
                                    int nextIndex = getNextPlayerIndex(currentPlayerIndex, numPlayers, clockwise);
                                    players.get(nextIndex).draw(deck, 2);
                                    currentPlayerIndex = nextIndex;
                                    printWithDelay("➕2️⃣ Player " + (currentPlayerIndex + 1) + " draws 2 cards and is skipped!", 1000);
                                } else if (face.equals("Draw Four")) {
                                    int nextIndex = getNextPlayerIndex(currentPlayerIndex, numPlayers, clockwise);
                                    players.get(nextIndex).draw(deck, 4);
                                    currentPlayerIndex = nextIndex;
                                    printWithDelay("➕4️⃣ Player " + (currentPlayerIndex + 1) + " draws 4 cards and is skipped!", 1000);
                                }
                            }

                            turnFinished = true;
                        } else {
                            printWithDelay("\n❌ Invalid move! That card cannot be played. Please try again.", 1000);
                        }
                    } else {
                        printWithDelay("\n❌ Invalid choice. Please enter a valid card index or 0.", 1000);
                    }
                } else {
                    printWithDelay("You have no playable cards! Drawing 1 card from the deck...", 1000);
                    currentHand.draw(deck, 1);
                    turnFinished = true;
                }
            }

            // Move to the next player if the game is still running
            if (gameRunning) {
                currentPlayerIndex = getNextPlayerIndex(currentPlayerIndex, numPlayers, clockwise);
            }
        }

        scanner.close();
    }

    /**
     * Calculates the index of the next player based on turn direction (clockwise or counter-clockwise).
     */
    public static int getNextPlayerIndex(int currentIndex, int numPlayers, boolean clockwise) {
        if (clockwise) {
            return (currentIndex + 1) % numPlayers;
        } else {
            return (currentIndex - 1 + numPlayers) % numPlayers;
        }
    }

    /**
     * Prints a message to the console and pauses execution for a specified delay.
     *
     * @param message The string to print.
     * @param millis  The delay in milliseconds (e.g., 1000 for 1 second).
     */
    public static void printWithDelay(String message, int millis) {
        System.out.println(message);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted.");
        }
    }
}
