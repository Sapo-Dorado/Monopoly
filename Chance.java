import java.util.Random;

/**
 * This class represents Chance squares on the Monopoly board. There is a static deck so it is
 * shared between all Chance squares and draws cards from it when the Player lands on the square.
 */
public class Chance extends Square
{
    static int[] deck = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    static int numCardsDrawn = 0;

    /**
     * This is the constructor for a Chance object. It creates a new square named Chance of type
     * DECK and shuffles the deck.
     */
    public Chance()
    {
        super("Chance", Square.DECK);
        shuffle();
    }

    /**
     * This method shuffles the deck by swapping each card with a random index in the array.
     */
    public static void shuffle()
    {
        int newPos;
        Random rand = new Random();
        int temp;
        for (int i = 0; i < deck.length; i++)
        {
            newPos = rand.nextInt(16);
            temp = deck[i];
            deck[i] = deck[newPos];
            deck[newPos] = temp;
        }
    }
    
    /**
     * This method returns the next card in the deck. If all the cards have been drawn it
     * reshuffles the deck and starts again.
     * 
     * @return an integer which represents the card being drawn.
     */
    public static int draw()
    {
        if (numCardsDrawn == 15)
        {
            numCardsDrawn = 0;
            shuffle();
        }
        return deck[numCardsDrawn++];
    }

}
