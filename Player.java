public class Player
{
    static Player[] playerList = new Player[8];
    static int playerCount = 0;
    private String name;
    public int position;
    private int money;
    private Property[] properties;
    private Property[] morgagedProps;
    private boolean isBankrupt;
    private int numRailroads;
    private int numUtilities;
    private int houseCount;
    private int hotelCount;
    private int getOutOfJailFree;
    private int numProps;
    private int numDoubles;
    
    public Player(String name)
    {
        this.name = name;
        this.position = 0;
        this.money = 1500;
        this.properties = new Property[Board.NUM_PROPERTIES];
        this.morgagedProps = new Property[Board.NUM_PROPERTIES];
        this.isBankrupt = false;
        this.numRailroads = -1;
        this.numUtilities = 0;
        this.getOutOfJailFree = 0;
        this.houseCount = 0;
        this.hotelCount = 0;
        this.numProps = 0;
        this.numDoubles = 0;
        playerList[playerCount] = this;
        playerCount++;
    }

    public void move()
    {
        int roll = Die.doubleRoll();
        if (roll > 12)
        {
            IO.display(name + " rolled a " + (roll - 100) + " which is a double!");
            numDoubles++;
            if (numDoubles == 3)
            {
                IO.display("Thats three doubles in a row. Now you go to jail.");
                goToJail();
                numDoubles = 0;
            }
            else
            {
                evaluateMove(roll - 100);
                move();
            }
        }
        else
        {
            IO.display(name + " rolled a " + roll + ".");
            evaluateMove(roll);
            numDoubles = 0;
        }
    }

    public void evaluateMove(int roll)
    {
        position += roll;
        if (position >= Board.NUM_SQUARES)
        {
            IO.display(name + " passed Go and earned $200.");
            IO.display(name + " now has $" + money);
            receive(200);
            position = position % Board.NUM_SQUARES;
        }
        evaluateSquare(roll);
    }

    public void evaluateSquare(int roll)
    {
        Square currentSquare = getCurrentSquare();
        int type = currentSquare.getType();
        IO.display(name + " landed on " + currentSquare);
        if (type == Square.STANDARD_PROPERTY)
        {
            evaluateStandard();
        }
        else if (type == Square.RAILROAD_PROPERTY)
        {
            evaluateRailroad();
        }
        else if (type == Square.UTILITY_PROPERTY)
        {
            evaluateUtility(roll);
        }
        else if (type == Square.DECK)
        {
            evaluateDeck(roll);
        }
        else if (type == Square.TAX)
        {
            evaluateTax();
        }
        else
        {
            evaluateCorner();
        }
    }

    public void evaluateStandard()
    {
        int owner = getOwner();
        if (owner == -1)
        {
            if (!buyPrompt())
            {
                resolveAuction();
            }
        }
        else
        {
            pay(Player.playerList[owner], ((StandardProperty)getCurrentSquare()).getCost());
        }
        finishTurn();
    }

    public void evaluateRailroad()
    {
        int owner = getOwner();
        if (owner == -1)
        {
            if(buyPrompt())
            {
                numRailroads++;
            }
            else
            {
                resolveAuction();
            }
        }
        else
        {
            Player recipient = Player.playerList[owner];
            pay(recipient, 25 * (int)Math.pow(2,recipient.getNumRailroads()));
        }
        finishTurn();
    }

    public void evaluateUtility(int roll)
    {
        int owner = getOwner();
        if (owner == -1)
        {
            if(!buyPrompt())
            {
                resolveAuction();
            }
            else
            {
                numUtilities++;
            }
        }
        else
        {
            Player recipient = Player.playerList[owner];
            int amount;
            if (recipient.getNumUtilities() == 1)
            {
                amount = roll * 4;
            }
            else
            {
                amount = roll * 10;
            }
            pay(recipient, amount);
        }
        finishTurn();
    }
    public void evaluateDeck(int roll)
    {
        if(getCurrentSquare().toString().equals("Community Chest"))
        {
            resolveCommunityChest(roll);
        }
        else
        {
            resolveChance(roll);
        }
    }
    public void evaluateTax()
    {
        forceSpend(((Tax)getCurrentSquare()).getFee());
    }
    public void evaluateCorner()
    {
        if(position == 30)
        {
            goToJail();
        }
        finishTurn();
    }

    public boolean buyPrompt()
    {
        String[] options = {"Yes","No"};
        int response = IO.prompt(name + ", do you want to buy this property?", options);
        if (response == 0)
        {
            if(spend(((Property)getCurrentSquare()).getPrice()) != -1)
            {
                gainProperty();
                IO.display(name + " successfully bought " + getCurrentSquare());
                return true;
            }
        }
        return false;
    }

    public void resolveAuction()
    {
    }

    public int getOwner()
    {
        int i = 0;
        for (Player p: playerList)
        {
            if((p != null) && (p.getProperties()[((Property)Board.squareList[position]).getPropertyNum()] != null))
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void goToJail()
    {
        position = 10;
        if(getOutOfJailFree > 0)
        {
            getOutOfJailFree--;
        }
        else
        {
            forceSpend(50);
        }
    }

    public Square getCurrentSquare()
    {
        return Board.squareList[position];
    }

    public int pay(Player recipient, int amount)
    {
        if (recipient == this)
        {
            return -1;
        }
        else if (amount > money)
        {
            money -= amount;
            if (goBankrupt())
            {
                recipient.receive(amount + money);
                IO.display(name + " payed " + recipient.getName() + " $" + (amount + money));
                return -1;
            }
            else
            {
                recipient.receive(amount);
                IO.display(name + " payed " + recipient.getName() + " $" + amount + " and has $" + money + " left");
                return 1;
            }
        }
        else
        {
            recipient.receive(amount);
            money -= amount;
            IO.display(name + " payed " + recipient.getName() + " $" + amount + " and has $" + money + " left");
            return 1;
        }
    }

    public void resolveChance(int roll)
    {
        int card = Chance.draw();
        IO.display("Your card was:");
        if (card == 0)
        {
            position = 0;
            receive(200);
            IO.display("Advance to Go, collect 200 dollars");
            evaluateSquare(roll);
        }
        else if (card == 1)
        {
            if(position > 24)
            {
                IO.display("Advance to Illinois Avenue and collect 200 dollars for passing go.");
            }
            else
            {
                IO.display("Advance to Illinois Avenue");
            }
            position = 24;
            evaluateSquare(roll);
        }
        else if (card == 2)
        {
            if (position > 11)
            {
                IO.display("Advance to St. Charles Place and collect 200 dollars for passing go.");
            }
            else
            {
                IO.display("Advance to St. Charles Place.");
            }
            position = 11;
            evaluateSquare(roll);
        }
        else if (card == 3)
        {
            IO.display("Advance to nearest Utility if owned roll the dice and pay owner 10 times the amount rolled");
            if (position > 28)
            {
                IO.display("You passed go and received 200 dollars");
                receive(200);
                position = 12;
            }
            else if (position < 12)
            {
                position = 12;
            }
            else 
            {
                position = 28;
            }
            int owner = getOwner();
            if (owner == -1)
            {
                evaluateSquare(roll);
            }
            else
            {
                pay(Player.playerList[owner], 10 * Die.roll());
                finishTurn();
            }
        }
        else if (card == 4 || card == 14)
        {
            IO.display("Advance to nearest Railroad and if owned pay owner twice the normal amount.");
            if (position > 35)
            {
                IO.display("You passed go and received 200 dollars");
                receive(200);
                position = 5;
            }
            else if (position < 15)
            {
                position = 15;
            }
            else
            {
                position = 25;
            }
            int owner = getOwner();
            if (owner == -1)
            {
                evaluateSquare(roll);
            }
            else
            {
                Player recipient = Player.playerList[owner];
                pay(recipient, 2 * (25 * (int)Math.pow(2,recipient.getNumRailroads())));
                finishTurn();
            }
        }
        else if (card == 5)
        {
            IO.display("The bank pays you a dividend of $50.");
            receive(50);
            finishTurn();
        }
        else if (card == 6)
        {
            IO.display("You have received a get out of jail free card.");
            getOutOfJailFree++;
            finishTurn();
        }
        else if (card == 7)
        {
            IO.display("Go back three spaces");
            position -= 3;
            evaluateSquare(roll);
        }
        else if (card == 8)
        {
            IO.display("Go to Jail");
            goToJail();
            finishTurn();
        }
        else if (card == 9)
        {
            IO.display("Pay $25 for each house and $100 for each hotel.");
            forceSpend(25 * houseCount + 100 * hotelCount);
            finishTurn();
        }
        else if (card == 10)
        {
            IO.display("Pay the poor tax of $15");
            forceSpend(15);
            finishTurn();
        }
        else if (card == 11)
        {
            IO.display("Take a trip to the Reading Railroad and collect $200 for passing go.");
            receive(200);
            position = 5;
            evaluateSquare(roll);
        }
        else if (card == 12)
        {
            IO.display("Take a walk on Boardwalk");
            position = 39;
            evaluateSquare(roll);
        }
        else if (card == 13)
        {
            IO.display("You have been elected chairman of the Board. Pay each player $50.");
            int count = 0;
            for (Player p: playerList)
            {
                if (p != null)
                {
                    count++;
                    p.receive(50);
                }
            }
            forceSpend(50 * count);
            finishTurn();
        }
        else
        {
            IO.display("Your building loan matures. You earn $150.");
            receive(150);
            finishTurn();
        }
    }

    public void resolveCommunityChest(int roll)
    {
        int card = CommunityChest.draw();
        IO.display("Your card was:");
        if (card == 0)
        {
            IO.display("Advance to go and collect $200");
            receive(200);
            position = 0;
            finishTurn();
        }
        else if (card == 1)
        {
            IO.display("Bank error in your favor. Collect $200.");
            receive(200);
            finishTurn();
        }
        else if (card == 2)
        {
            IO.display("Doctor's fees. Pay $50.");
            forceSpend(50);
            finishTurn();
        }
        else if (card == 3)
        {
            IO.display("You earn $50 from stock.");
            receive(50);
            finishTurn();
        }
        else if (card == 4)
        {
            IO.display("You earn a get out of jail free card.");
            getOutOfJailFree++;
            finishTurn();
        }
        else if (card == 5)
        {
            IO.display("Go to jail");
            goToJail();
            finishTurn();
        }
        else if (card == 6)
        {
            IO.display("Grand Opera Night. Collect $50 from each player.");
            for (Player p: playerList)
            {
                p.pay(this, 50);
            }
            finishTurn();
        }
        else if (card == 7)
        {
            IO.display("Holiday fund matures. Collect $100");
            receive(100);
            finishTurn();
        }
        else if (card == 8)
        {
            IO.display("Income Tax refund. Collect $20.");
            receive(20);
            finishTurn();
        }
        else if (card == 9)
        {
            IO.display("Life insurance matures. Collect $100.");
            receive(100);
            finishTurn();
        }
        else if (card == 10)
        {
            IO.display("Hospital fees. Pay $50");
            forceSpend(50);
            finishTurn();
        }
        else if (card == 11)
        {
            IO.display("School fees. Pay $50.");
            forceSpend(50);
            finishTurn();
        }
        else if (card == 12)
        {
            IO.display("Receive $20 consulting fee");
            receive(20);
            finishTurn();
        }
        else if (card == 13)
        {
            IO.display("You are assessed for street repairs. Pay $40 per house and $115 per hotel");
            forceSpend(40 * houseCount + 115 * hotelCount);
            finishTurn();
        }
        else if (card == 14)
        {
            IO.display("You have won second prize in a beauty contest. Collect $10.");
            receive(10);
            finishTurn();
        }
        else
        {
            IO.display("You inherit $100");
            receive(100);
            finishTurn();
        }
    }

    public int spend(int amount)
    {
        if (amount > money)
        {
            IO.display("Too expensive, purchase canceled");
            return -1;
        }
        else
        {
            money -= amount;
            IO.display(name + " payed $" + amount + " and has $" + money + " remaining.");
            return 1;
        }
    }

    public int forceSpend(int amount)
    {
        if (amount > money)
        {
            money -= amount;
            goBankrupt();
            return -1;
        }
        else
        {
            money -= amount;
            IO.display(name + " payed $" + amount + " and has $" + money + " remaining.");
            return 1;
        }
    }
    public void finishTurn()
    {
        if(!isBankrupt)
        {
            printStatus();
            String[] choices = {"Buy houses/hotels","Offer a trade", "End your turn"};
            int choice = IO.prompt("What would you like to do?", choices);
            if (choice == 0)
            {
                resolveHousingPurchase();
            }
            else if (choice == 1)
            {
                resolveTrade();
            }
            else
            {
                IO.display("");
            }
        }
    }

    public void resolveHousingPurchase()
    {
        int[][] sets = findSets();
        int setCount = 0;
        for(int i = 0; i < sets.length; i++)
        {
            if(sets[i] != null)
            {
                String[] choices;
                if(sets[i].length == 2)
                {
                    choices = new String[3];
                    choices[0] = "None of these";
                    choices[1] = properties[sets[i][0]].toString();
                    choices[2] = properties[sets[i][1]].toString();
                }
                else
                {
                    choices = new String[4];
                    choices[0] = "None of these";
                    choices[1] = properties[sets[i][0]].toString();
                    choices[2] = properties[sets[i][1]].toString();
                    choices[3] = properties[sets[i][2]].toString();
                }
                int response = IO.prompt("Would you like to purchase houses/hotels on:", choices);
                if(response != 0)
                {
                    buyProperty(properties[sets[i][response - 1]]);
                    i--;
                }
            }
        }
        IO.display("All possible building opportunities have been exhausted.");
        finishTurn();
    }

    public void buyProperty(Property prop)
    {
        int potentialPurchases = 7 - ((StandardProperty)prop).getDevelopment();
        String[] choices = new String[potentialPurchases];
        for (int i = 0; i < potentialPurchases; i++)
        {
            choices[i] = "" + i + " ($" + (((StandardProperty)prop).getBuildingCost() *  i) + ")";
        }
        int response = IO.prompt("How many stages of development do you want to buy?", choices);
        if(spend(response * ((StandardProperty)prop).getBuildingCost()) != -1)
        {
            ((StandardProperty)prop).develop(response);
            IO.display("You successfully developed your property " + response + " times.");
        }
    }

    public int[][] findSets()
    {
        int[][] sets = new int[8][];
        int[][] setPositions = Board.sets;
        for(int i = 0; i < setPositions.length; i++)
        {
            if (checkNulls(setPositions[i]))
            {
                sets[i] = setPositions[i];
            }
        }
        return sets;
    }

    public boolean checkNulls(int[] props)
    {
        for(int i = 0; i < props.length; i++)
        {
            if (properties[props[i]] == null)
            {
                return false;
            }
        }
        return true;
    }
    public void resolveTrade()
    {
    }

    public void printStatus()
    {
        IO.display("");
        IO.display("Here is a summary of " + name + "'s assets:");
        IO.display("Money:");
        IO.display("$" + money);
        IO.display("Properties:");
        for (Property p: properties)
        {
            if(p != null)
            {
                String message = p.toString();
                if (p.getType() == Square.STANDARD_PROPERTY)
                {
                    int development = ((StandardProperty)p).getDevelopment();
                    if (development > 1 && development < 6)
                    {
                        message += " (Houses: " + (development - 1) + ")";
                    }
                    else if (development == 6)
                    {
                        message += " (Hotel)";
                    }
                }
                IO.display(message);
            }
        }
        IO.display("Morgaged Properties:");
        for (Property p: morgagedProps)
        {
            if(p != null)
            {
                IO.display(p.toString());
            }
        }
        IO.display("");
    }

    public boolean morgage()
    {
        IO.display(name + " owes $" + Math.abs(money));
        if (numProps > 0)
        {
            String[] choices = new String[numProps];
            int count = 0;
            int addition = 0;
            int[] propertyLocs = new int[numProps];
            int[] propertyVals = new int[numProps];
            for(int i = 0; i < properties.length; i++)
            {
                if (properties[i] != null)
                {
                    if (properties[i].getType() == Square.STANDARD_PROPERTY && ((StandardProperty)properties[i]).getDevelopment() > 1)
                    {
                        addition += (((StandardProperty)properties[i]).getDevelopment() - 1) / 2;
                    }
                    choices[count] = properties[i].toString() + " (" + (addition + properties[i].getPrice() / 2) + ")";
                    propertyLocs[count] = i;
                    propertyVals[count] = properties[i].getPrice() / 2;
                }
                addition = 0;
            }
            int choice = IO.prompt("Which property would you like to morgage?", choices);
            Property thisProp = properties[propertyLocs[choice]];
            if (loseProperty(thisProp))
            {
                money += propertyVals[choice];
                morgagedProps[propertyLocs[choice]] = thisProp;
            }
            if(money >= 0)
            {
                return true;
            }
            else
            {
                return morgage();
            }
        }
        else
        {
            return false;
        }
    }

    
    public boolean goBankrupt()
    {
        if(!morgage())
        {
            IO.display(name + " went Bankrupt");
            for(int i = 0; i < playerList.length; i++)
            {
                if (playerList[i] == this)
                {
                    playerList[i] = null;
                }
            }
            isBankrupt = true;
            return true;
        }
        return false;
    }

    public void gainProperty()
    {
        Property thisProp = (Property)getCurrentSquare();
        properties[thisProp.getPropertyNum()] = thisProp;
        numProps++;
        if(thisProp.getType() == Square.STANDARD_PROPERTY)
        {
            int[] thisSet = Board.sets[((StandardProperty)thisProp).getSetNumber()];
            if(checkNulls(thisSet))
            {
                for (int i = 0; i < thisSet.length; i++)
                {
                    ((StandardProperty)properties[thisSet[i]]).develop(1);
                }
                IO.display("You now have a set!");
            }
        }
    }

    public boolean loseProperty(Property prop)
    {
        if(prop.getType() == Square.STANDARD_PROPERTY)
        {
            int[] thisSet = Board.sets[((StandardProperty)prop).getSetNumber()];
            if(checkNulls(thisSet))
            {
                String[] choices = {"Yes", "No"};
                int choice = IO.prompt("You will have to sacrifice your set and morgage any developments on this property. Are you sure you want to do this?", choices);
                if(choice == 0)
                {
                    for(int i = 0; i < thisSet.length; i++)
                    {
                        StandardProperty thisProp = ((StandardProperty)properties[thisSet[i]]);
                        money += (thisProp.getDevelopment() - 1) * (thisProp.getBuildingCost() / 2);
                        thisProp.resetDevelopment();
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        properties[prop.getPropertyNum()] = null;
        numProps--;
        return true;
    }

    public void receive(int amount)
    {
        money += amount;
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

    public Property[] getProperties()
    {
        return properties;
    }

    public int getNumRailroads()
    {
        return numRailroads;
    }
    public int getNumUtilities()
    {
        return numUtilities;
    }
}
