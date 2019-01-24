public class Driver
{
    public static void main(String[] args)
    {
        boolean done = false;
        IO.display("Please enter your names and passwords one at a time. Type done to finish");
        for (int i = 0; i <= 8 && !done; i++)
        {
            String name = IO.readString("Please enter name #" + (i + 1) + " correctly. Type done to finish.");
            if (name.equals("done"))
            {
                if(i < 2)
                {
                    IO.display("You need at least 2 players. Please enter a valid name.");
                }
                else
                {
                    done = true;
                }
            }
            else
            {
                new Player(name, IO.readString(name + ", please enter your password"));
                IO.display("Please enter the next name");
            }
        }
        while(Player.getRemainingPlayers() > 1)
        {
            for (Player p: Player.playerList)
            {
                if(p != null)
                {
                    p.move();
                }
            }
        }
    }
}
