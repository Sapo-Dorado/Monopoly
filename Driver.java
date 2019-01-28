public class Driver
{
    public static void newGame()
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
    }

    public static void loadGame()
    {
        String saveName = IO.readString("What is the name of your save");
        String[] data = IO.readSave(saveName);
        int i = 0;
        while (i < data.length)
        {
            
            boolean inJail;
            if(data[4].equals("1"))
            {
                inJail = true;
            }
            else
            {
                inJail = false;
            }
            int numProps = Integer.parseInt(data[i + 11]);
            int numMortgagedProps = Integer.parseInt(data[i + 12]);
            Property[] properties = new Property[numProps];
            Property[] mortgagedProps = new Property[numMortgagedProps];
            for (int j = 0; j < numProps; j++)
            {
                properties[j] = (Property)Board.squareList[Board.propertyLocs[Integer.parseInt(data[i + 13 + j])]];
            }
            for (int j = 0; j < numMortgagedProps; j++)
            {
                mortgagedProps[j] = (Property)Board.squareList[Board.propertyLocs[Integer.parseInt(data[i + 13 + numProps + j])]];
            }
            System.out.println(data[i+10]);
            new Player(data[i], data[i + 1], Integer.parseInt(data[i + 2]), Integer.parseInt(data[i + 3]),
                    properties, mortgagedProps, inJail, Integer.parseInt(data[i + 5]), Integer.parseInt(data[i + 6]),
                    Integer.parseInt(data[i + 7]), Integer.parseInt(data[i + 8]), Integer.parseInt(data[i + 9]),
                    Integer.parseInt(data[i + 10]));
            i += 13 + numProps + numMortgagedProps;
        }
    }
    
    public static void main(String[] args)
    {
        String[] choices = {"New", "Load"};
        int choice = IO.prompt("Would you like to start a new game or load a previous game?", choices);
        if (choice == 0)
        {
            newGame();
        }
        else
        {
            loadGame();
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
