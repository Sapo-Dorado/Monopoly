import java.util.Random;

public class Die
{
    public static int roll()
    {
        Random die = new Random();
        int roll1 = die.nextInt(6) + 1;
        int roll2 = die.nextInt(6) + 1;
        return roll1 + roll2;
    }

    public static int doubleRoll()
    {
        Random die = new Random();
        int roll1 = die.nextInt(6) + 1;
        int roll2 = die.nextInt(6) + 1;
        if (roll1 == roll2)
        {
            return roll1 + roll2 + 100;
        }
        else
        {
            return roll1 + roll2;
        }
    }
}

