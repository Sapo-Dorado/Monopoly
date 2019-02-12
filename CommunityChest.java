import java.util.Random;

public class CommunityChest extends Square
{
    static int[] deck = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    static int numCardsDrawn = 0;

    public CommunityChest()
    {
        super("Community Chest", Square.DECK);
        shuffle();
    }

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
