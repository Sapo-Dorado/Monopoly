public class Player
{
    private String name;
    private int position;
    private int money;
    
    public Player(String name)
    {
        this.name = name;
        this.position = 0;
    }

    public int move()
    {
        int roll = Die.roll();
        position += roll;
        if (position >= Board.NUM_SQUARES)
        {
            position = position % Board.NUM_SQUARES;
        }
        return roll;
    }

    public String getName()
    {
        return name;
    }

    public int getPosition()
    {
        return position;
    }

    public int getMoney()
    {
        return money;
    }
}
