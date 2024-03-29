public class Player
{
    static Player[] playerList = new Player[8];
    static int playerCount = 0;
    private String name;
    private String password;
    public int position;
    public int money;
    private boolean isBankrupt;
    private boolean inJail;
    private int turnsInJail;
    private int numRailroads;
    private int numUtilities;
    private int houseCount;
    private int hotelCount;
    private int getOutOfJailFree;
    private int numProps;
    private int numMortgagedProps;
    private int numDoubles;
    private Property[] properties;
    private Property[] mortgagedProps;

    public static int getRemainingPlayers()
    {
        int count = 0;
        for (Player p: playerList)
        {
            if (p != null)
            {
                count++;
            }
        }
        return count;
    }

    public Player(String name, String password)
    {
        this.name = name;
        this.password = password;
        this.position = 0;
        this.money = 1500;
        this.properties = new Property[Board.NUM_PROPERTIES];
        this.mortgagedProps = new Property[Board.NUM_PROPERTIES];
        this.isBankrupt = false;
        this.inJail = false;
        this.turnsInJail = 0;
        this.numRailroads = -1;
        this.numUtilities = 0;
        this.getOutOfJailFree = 0;
        this.houseCount = 0;
        this.hotelCount = 0;
        this.numProps = 0;
        this.numMortgagedProps = 0;
        this.numDoubles = 0;
        playerList[playerCount] = this;
        playerCount++;
    }

    public Player(String name, String password, int position, int money, Property[] properties, Property[] mortgagedProps, boolean inJail, int turnsInJail, int numRailroads, int numUtilities, int houseCount, int hotelCount,
            int getOutOfJailFree)
    {
        this.name = name;
        this.password = password;
        this.position = position;
        this.money = money;
        this.properties = new Property[Board.NUM_PROPERTIES];
        this.mortgagedProps = new Property[Board.NUM_PROPERTIES];
        for(Property p: properties)
        {
            this.properties[p.getPropertyNum()] = p;
        }
        for(Property p: mortgagedProps)
        {
            this.mortgagedProps[p.getPropertyNum()] = p;
        }
        this.isBankrupt = false;
        this.inJail = inJail;
        this.turnsInJail = turnsInJail;
        this.numRailroads = numRailroads;
        this.numUtilities = numUtilities;
        this.getOutOfJailFree = getOutOfJailFree;
        this.houseCount = houseCount;
        this.hotelCount = hotelCount;
        this.numProps = properties.length;
        this.numMortgagedProps = mortgagedProps.length;
        this.numDoubles = 0;
        playerList[playerCount] = this;
        playerCount++;
    }


    public void move()
    {
        String[] onlyChoice = {"Roll"};
        String message;
        if (numDoubles == 0)
        {
            Board.printBoard();
            message = name + "'s turn.";
        }
        else if (numDoubles == 1)
        {
            message = name + "'s second turn.";
        }
        else
        {
            message = name + "'s third turn.";
        }
        IO.prompt(message, onlyChoice); 
        int roll = Die.doubleRoll();
        if (inJail)
        {
            if (roll > 12)
            {
                IO.display(name + " rolled a " + (roll - 100) + " which is a double and gets them out of jail!");
                inJail = false;
                turnsInJail = 0;
                roll = roll - 100;
                evaluateMove(roll);
                finishTurn();
            }
            else
            {
                if (turnsInJail == 3)
                {
                    leaveJail();
                    evaluateMove(roll);
                    finishTurn();
                }
                else
                {
                    String[] choices = {"Yes", "No"};
                    IO.display(name + " rolled a " + roll + " which is not a double. :(");
                    if (IO.prompt("Do you want to leave Jail early?\nYou have $" + money + " and it will cost $50.", choices) == 0)
                    {
                        leaveJail();
                        evaluateMove(roll);
                        finishTurn();
                    }
                    else
                    {
                        turnsInJail++;
                        finishTurn();
                    }
                }
            }
        }
        else
        {
            if (roll > 12)
            {
                IO.display(name + " rolled a " + (roll - 100) + " which is a double!");
                numDoubles++;
                if (numDoubles == 3)
                {
                    IO.display("Thats three doubles in a row. Now you go to jail.");
                    goToJail();
                    numDoubles = 0;
                    finishTurn();
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
                finishTurn();
            }
        }
    }

    public void evaluateMove(int roll)
    {
        position += roll;
        if (position >= Board.NUM_SQUARES)
        {
            IO.display(name + " passed Go and earned $200.");
            receive(200);
            IO.display(name + " now has $" + money);
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
        int owner = getOwner((Property)getCurrentSquare());
        if (owner == -9)
        {
            if (!buyPrompt())
            {
                resolveAuction((Property)getCurrentSquare(), "Normal");
            }
        }
        else if (owner < 0)
        {
            IO.display("This property was mortgaged so no rent is due.");
        }
        else
        {
            pay(Player.playerList[owner], ((StandardProperty)getCurrentSquare()).getCost());
        }
    }

    public void evaluateRailroad()
    {
        int owner = getOwner((Property)getCurrentSquare());
        if (owner == -9)
        {
            if(!buyPrompt())
            {
                resolveAuction((Property)getCurrentSquare(), "Normal");
            }
        }
        else if (owner < 0)
        {
            IO.display("This property was mortgaged so no rent is due.");
        }
        else
        {
            Player recipient = Player.playerList[owner];
            pay(recipient, 25 * (int)Math.pow(2,recipient.getNumRailroads()));
        }
    }

    public void evaluateUtility(int roll)
    {
        int owner = getOwner((Property)getCurrentSquare());
        if (owner == -9)
        {
            if(!buyPrompt())
            {
                resolveAuction((Property)getCurrentSquare(), "Normal");
            }
        }
        else if (owner < 0)
        {
            IO.display("This property was mortgaged so no rent is due.");
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
    }

    public boolean buyPrompt()
    {
        String[] options = {"Yes","No"};
        Property thisProp = ((Property)getCurrentSquare());
        IO.display(name + " has $" + money + " and " + thisProp.toString() + " costs " + thisProp.getPrice() + ".");
        int response = IO.prompt(name + ", do you want to buy this property?", options);
        if (response == 0)
        {
            if(spend(thisProp.getPrice()))
            {
                gainProperty(thisProp);
                IO.display(name + " successfully bought " + getCurrentSquare());
                return true;
            }
        }
        return false;
    }

    public void resolveAuction(Property prop, String type)
    {
        Player[] potentialBidders = new Player[8];
        int i = 0;
        for(Player p: playerList)
        {
            if (p != null)
            {
                potentialBidders[i] = p;
                i++;
            }
        }
        int numLeft = 1;
        int bid = 0;
        Player highestBidder;
        if (type.equals("Normal") && !isBankrupt)
        {
            highestBidder = this;
        }
        else
        {
            highestBidder = potentialBidders[0];
        }
        boolean done = false;
        String[] choices = {"Yes", "No"};
        IO.display("");
        if (type.equals("Normal"))
        {
            IO.display(prop + " is on auction. If nobody bids, " + highestBidder.toString() + " gets it for free");
        }
        else
        {
            IO.display("The mortgaged property " + prop + " is on auction. If nobody bids, " + highestBidder.toString() + " gets it for free");
        }
        while(!done)
        {
            for(i = 0; i < potentialBidders.length; i++)
            {
                if (potentialBidders[i] != null && potentialBidders[i] != highestBidder)
                {
                    int choice = IO.prompt(potentialBidders[i].getName() + ", the current bid is $" + bid + " would you like to raise?", choices);
                    if (choice == 0)
                    {
                        bid = IO.getOffer("How much would you like to bid?", bid);
                        highestBidder = potentialBidders[i];
                        numLeft++;
                    }
                    else
                    {
                        potentialBidders[i] = null;
                    }
                }
            }
            if (numLeft == 0)
            {
                done = true;
            }
            numLeft = 0;
        }
        if (highestBidder.forceSpend(bid))
        {
            if (type.equals("Normal"))
            {
                highestBidder.gainProperty(prop);
            }
            else
            {
                highestBidder.gainMortgagedProperty(prop);
            }
        }
        else
        {
            resolveAuction(prop, type);
        }
    }

    public int getOwner(Property prop)
    {
        int i = 0;
        for (Player p: playerList)
        {
            if((p != null) && (p.getProperties()[prop.getPropertyNum()] != null))
            {
                return i;
            }
            i++;
        }
        i = 0;
        for (Player p: playerList)
        {
            if((p != null) && (p.getMortgagedProperties()[prop.getPropertyNum()] != null))
            {
                return -i - 1;
            }
            i++;
        }
        return -9;
    }

    public void goToJail()
    {
        position = 10;
        inJail = true;
    }

    public void leaveJail()
    {
        if(getOutOfJailFree > 0)
        {
            IO.display("You used a get out of jail free card. You have " + --getOutOfJailFree + " left.");
        }
        else
        {
            forceSpend(50);
        }
        turnsInJail = 0;
        inJail = false;
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
                recipient.receive(money);
                giveAllStuff(recipient);
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
                receive(200);
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
                receive(200);
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
            int owner = getOwner((Property)getCurrentSquare());
            if (owner < 0)
            {
                evaluateSquare(roll);
            }
            else
            {
                IO.display(name + " landed on " + getCurrentSquare().toString() + ".");
                pay(Player.playerList[owner], 10 * Die.roll());
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
            int owner = getOwner((Property)getCurrentSquare());
            if (owner < 0)
            {
                evaluateSquare(roll);
            }
            else
            {
                IO.display(name + " landed on " + getCurrentSquare().toString() + ".");
                Player recipient = Player.playerList[owner];
                pay(recipient, 2 * (25 * (int)Math.pow(2,recipient.getNumRailroads())));
            }
        }
        else if (card == 5)
        {
            IO.display("The bank pays you a dividend of $50.");
            receive(50);
        }
        else if (card == 6)
        {
            IO.display("You have received a get out of jail free card.");
            getOutOfJailFree++;
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
        }
        else if (card == 9)
        {
            IO.display("Pay $25 for each house and $100 for each hotel.");
            forceSpend(25 * houseCount + 100 * hotelCount);
        }
        else if (card == 10)
        {
            IO.display("Pay the poor tax of $15");
            forceSpend(15);
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
                if (p != null && p != this)
                {
                    count++;
                    p.receive(50);
                }
            }
            forceSpend(50 * count);
        }
        else
        {
            IO.display("Your building loan matures. You earn $150.");
            receive(150);
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
        }
        else if (card == 1)
        {
            IO.display("Bank error in your favor. Collect $200.");
            receive(200);
        }
        else if (card == 2)
        {
            IO.display("Doctor's fees. Pay $50.");
            forceSpend(50);
        }
        else if (card == 3)
        {
            IO.display("You earn $50 from stock.");
            receive(50);
        }
        else if (card == 4)
        {
            IO.display("You earn a get out of jail free card.");
            getOutOfJailFree++;
        }
        else if (card == 5)
        {
            IO.display("Go to jail");
            goToJail();
        }
        else if (card == 6)
        {
            IO.display("Grand Opera Night. Collect $50 from each player.");
            for (Player p: playerList)
            {
                if (p != null && p != this)
                {
                    p.pay(this, 50);
                }
            }
        }
        else if (card == 7)
        {
            IO.display("Holiday fund matures. Collect $100");
            receive(100);
        }
        else if (card == 8)
        {
            IO.display("Income Tax refund. Collect $20.");
            receive(20);
        }
        else if (card == 9)
        {
            IO.display("Life insurance matures. Collect $100.");
            receive(100);
        }
        else if (card == 10)
        {
            IO.display("Hospital fees. Pay $50");
            forceSpend(50);
        }
        else if (card == 11)
        {
            IO.display("School fees. Pay $50.");
            forceSpend(50);
        }
        else if (card == 12)
        {
            IO.display("Receive $20 consulting fee");
            receive(20);
        }
        else if (card == 13)
        {
            IO.display("You are assessed for street repairs. Pay $40 per house and $115 per hotel");
            forceSpend(40 * houseCount + 115 * hotelCount);
        }
        else if (card == 14)
        {
            IO.display("You have won second prize in a beauty contest. Collect $10.");
            receive(10);
        }
        else
        {
            IO.display("You inherit $100");
            receive(100);
        }
    }

    public boolean spend(int amount)
    {
        if (amount > money)
        {
            String[] choices = {"Yes", "No"};
            int worth = money;
            int choice;
            for (Property p: properties)
            {
                if (p != null)
                {
                    if (p.getType() == Square.STANDARD_PROPERTY && ((StandardProperty)p).getDevelopment() > 1)
                    {
                        worth += (((StandardProperty)p).getDevelopment() - 1) + (((StandardProperty)p).getBuildingCost() / 2);
                    }
                    worth += p.getPrice() / 2;
                }
            }
            if (worth >= amount)
            {
                choice = IO.prompt("Are you sure? You will have to mortgage properties to pay for this.", choices);
            }
            else
            {
                choice = IO.prompt("This is a terrible idea, you can only pay $" + worth + " out of $" + amount
                        + " even if you mortgage everything. You WILL go bankrupt if you press yes. Are you sure?",
                        choices);
            }
            if (choice == 0)
            {
                money -= amount;
                if (!goBankrupt())
                {
                    return true;
                }
                else
                {
                    auctionAllStuff();
                    return false;
                }
            }
            else
            {
                IO.display("Good choice.");
                return false;
            }
        }
        else
        {
            money -= amount;
            IO.display(name + " payed $" + amount + " and has $" + money + " remaining.");
            return true;
        }
    }

    public boolean spend(int amount, Property prop)
    {
        if (amount > money)
        {
            String[] choices = {"Yes", "No"};
            int[] thisSet = Board.sets[((StandardProperty)prop).getSetNumber()];
            int worth = money;
            int choice;
            for (int i = 0; i < properties.length; i++)
            {
                if (properties[i] != null && !isInArray(i, thisSet))
                {
                    if (((StandardProperty)properties[i]).getDevelopment() > 1)
                    {
                        worth += (((StandardProperty)properties[i]).getDevelopment() - 1) +
                            (((StandardProperty)properties[i]).getBuildingCost() / 2);
                    }
                    worth += properties[i].getPrice() / 2;
                }
            }
            if (worth >= amount)
            {
                choice = IO.prompt("Are you sure? You will have to mortgage properties to pay for this.", choices);
            }
            else
            {
                choice = IO.prompt("This is a terrible idea, you can only pay $" + worth + " out of $" + amount
                        + " even if you mortgage everything. You WILL go bankrupt if you press yes. Are you sure?",
                        choices);
            }
            if (choice == 0)
            {
                money -= amount;
                if (mortgage(prop))
                {
                    return true;
                }
                else
                {
                    for(int i: thisSet)
                    {
                        numProps--;
                        mortgagedProps[i] = properties[i];
                        properties[i] = null;
                    }
                    bankruptcyProcedures();
                    return false;
                }
            }
            else
            {
                IO.display("Good choice.");
                return false;
            }
        }
        else
        {
            money -= amount;
            IO.display(name + " payed $" + amount + " and has $" + money + " remaining.");
            return true;
        }
    }

    public boolean forceSpend(int amount)
    {
        if (amount > money)
        {
            money -= amount;
            if (goBankrupt())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            money -= amount;
            IO.display(name + " payed $" + amount + " and has $" + money + " remaining.");
            return true;
        }
    }
    public void finishTurn()
    {
        if(!isBankrupt)
        {
            printStatus();
            String[] choices = {"Save Game", "Buy houses/hotels","Buy back mortgaged properties","Offer a trade","View Properties","End your turn"};
            int choice = IO.prompt("What would you like to do?", choices);
            if (choice == 0)
            {
                if (isLastPlayer())
                {
                    String saveName = IO.readString("What do you want to call your save?");
                    IO.saveGame(saveName, getGameData());
                }
                else
                {
                    IO.display("You can only save when everybody has taken an equal number of turns");
                }
                finishTurn();
            }
            else if (choice == 1)
            {
                resolveHousingPurchase();
            }
            else if (choice == 2)
            {
                buyBackMortgagedProps();
            }
            else if (choice == 3)
            {
                resolveTrade();
            }
            else if (choice == 4)
            {
                printProperties();
            }
            else
            {
                IO.display("");
            }
        }
    }

    public void buyBackMortgagedProps()
    {
        if (numMortgagedProps > 0)
        {
            String[] choices = new String[numMortgagedProps + 1];
            int[] propertyLocs = new int[numMortgagedProps];
            int count = 1;
            choices[0] = "None of these";
            for(int i = 0; i < mortgagedProps.length; i++)
            {
                if (mortgagedProps[i] != null)
                {
                    choices[count] = mortgagedProps[i].toString() + " (" + mortgagedProps[i].getPrice() + ")";
                    propertyLocs[count - 1] = i;
                    count++;
                }
            }
            int choice = IO.prompt("Which property do you want to buy back?", choices);
            if (choice == 0)
            {
                finishTurn();
            }
            else
            {
                Property thisProp = mortgagedProps[propertyLocs[choice - 1]];
                if (spend(thisProp.getPrice()))
                {
                    gainProperty(thisProp);
                    mortgagedProps[thisProp.getPropertyNum()] = null;
                    numMortgagedProps--;
                }
            }
        }
        else
        {
            IO.display("No mortgaged properties to buy.");
        }
        finishTurn();
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
                    buyBuildings(properties[sets[i][response - 1]]);
                    if (isBankrupt)
                    {
                        break;
                    }
                    i--;
                }
            }
        }
        if(!isBankrupt)
        {
            IO.display("All possible building opportunities have been exhausted.");
            finishTurn();
        }
    }

    public void buyBuildings(Property prop)
    {
        int potentialPurchases = 7 - ((StandardProperty)prop).getDevelopment();
        String[] choices = new String[potentialPurchases];
        for (int i = 0; i < potentialPurchases; i++)
        {
            choices[i] = "" + i + " ($" + (((StandardProperty)prop).getBuildingCost() *  i) + ")";
        }
        int response = IO.prompt("How many stages of development do you want to buy?", choices);
        if(spend(response * ((StandardProperty)prop).getBuildingCost(), prop))
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
        String[] choices = new String[getRemainingPlayers() - 1];
        int[] playerLocs = new int[getRemainingPlayers() - 1];
        int i = 0;
        int loc = 0;
        for (Player p: playerList)
        {
            if (p != null && p != this)
            {
                choices[i] = p.toString();
                playerLocs[i] = loc;
                i++;
            }
            loc++;
        }
        int choice = IO.prompt("Who do you want to trade with?", choices);
        Player partner = playerList[playerLocs[choice]];
        Trade trade1 = createTrade(new Trade());
        Trade trade2 = partner.createTrade(new Trade());
        if(!trade1.isCancelled() && !trade2.isCancelled())
        {
            IO.display("");
            IO.display("Just to summarize, " + name + "'s trade:");
            IO.display(trade1.toString());
            IO.display("");
            IO.display("Just to summarize, " + partner.getName() + "'s trade:");
            IO.display(trade2.toString());
            IO.display("");
            IO.display("Please verify your passwords to confirm");
            if (verifyPassword() && partner.verifyPassword())
            {
                finalizeTrade(trade1, trade2);
                partner.finalizeTrade(trade2, trade1);
                if(trade1.getMoney() > 0)
                {
                    pay(partner, trade1.getMoney());
                }
                if(trade2.getMoney() > 0)
                {
                    partner.pay(this, trade2.getMoney());
                }
            }
            partner.printStatus();
            finishTurn();
        }
        else
        {
            IO.display("The trade was cancelled");
            finishTurn();
        }
    }

    public void finalizeTrade(Trade lose, Trade get)
    {
        for (Property p: lose.getProps())
        {
            if (p != null)
            {
                loseProperty(p);
            }
        }
        for (Property p: lose.getMortgagedProps())
        {
            if (p != null)
            {
                loseMortgagedProperty(p);
            }
        }
        getOutOfJailFree -= lose.getNumJail();
        for (Property p: get.getProps())
        {
            if (p != null)
            {
                gainProperty(p);
            }
        }
        for (Property p: get.getMortgagedProps())
        {
            if (p != null)
            {
                gainMortgagedProperty(p);
            }
        }
        getOutOfJailFree += get.getNumJail();
    }
        
    public Trade createTrade(Trade trade)
    {
        IO.display(name + "'s Trade:");
        IO.display(trade.toString());
        String[] choices = new String[numProps + numMortgagedProps + getOutOfJailFree + 3 - trade.getNumProps() - trade.getNumJail()];
        choices[0] = "No more";
        choices[1] = "Cancel trade";
        choices[2] = "Add money";
        int count = 3;
        int[] propertyLocs = new int[choices.length - 2];
        for (Property p: properties)
        {
            if (p != null && !trade.includesProp(p))
            {
                choices[count] = p.toString();
                propertyLocs[count - 3] = p.getPropertyNum();
                count++;
            }
        }
        int standardCutoff = count;
        for (Property p: mortgagedProps)
        {
            if (p != null && !trade.includesMortgagedProp(p))
            {
                choices[count] = p.toString() + " (Mortgaged)";
                propertyLocs[count - 3] = p.getPropertyNum();
                count++;
            }
        }
        int propertyCutoff = count;
        for (int i = getOutOfJailFree - trade.getNumJail(); i > 0; i--)
        {
            choices[count] = "Get out of Jail Free Card";
            count++;
        }
        int choice = IO.prompt("Which properties would you like to add to the trade", choices);
        if (choice == 0)
        {
            if (trade.isEmpty())
            {
                IO.display("You can't have an empty trade");
                return createTrade(trade);
            }
            else
            {
                return trade;
            }
        }
        else if (choice == 1)
        {
            trade.cancel();
            return trade;
        }
        else if (choice == 2)
        {
            trade.addMoney(IO.getAmount("This trade has $" + trade.getMoney() + " included. What would you like to add. (enter a negative to remove money)"));
            return createTrade(trade);
        }
        else if (choice >= propertyCutoff)
        {
            trade.addGetOutOfJailFree();
            return createTrade(trade);
        }
        else
        {
            int loc = propertyLocs[choice - 3];
            if(choice >= standardCutoff)
            {
                trade.addMortgaged(mortgagedProps[loc]);
            }
            else
            {
                trade.add(properties[loc]);
            }
            return createTrade(trade);
        }
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
        for (Property p: mortgagedProps)
        {
            if(p != null)
            {
                IO.display(p.toString());
            }
        }
        IO.display("Get out of jail free cards: " + getOutOfJailFree);
        IO.display("");
    }

    public boolean mortgage()
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
                    count++;
                }
                addition = 0;
            }
            int choice = IO.prompt("Which property would you like to mortgage?", choices);
            Property thisProp = properties[propertyLocs[choice]];
            if (loseProperty(thisProp))
            {
                money += propertyVals[choice];
                numMortgagedProps++;
                mortgagedProps[thisProp.getPropertyNum()] = thisProp;
            }
            if(money >= 0)
            {
                return true;
            }
            else
            {
                return mortgage();
            }
        }
        else
        {
            return false;
        }
    }

    public boolean mortgage(Property prop)
    {
        IO.display(name + " owes $" + Math.abs(money));
        int[] thisSet = Board.sets[((StandardProperty)prop).getSetNumber()];
        int numberOfProps = numProps - thisSet.length;
        if (numberOfProps > 0)
        {
            String[] choices = new String[numberOfProps];
            int count = 0;
            int addition = 0;
            int[] propertyLocs = new int[numberOfProps];
            int[] propertyVals = new int[numberOfProps];
            for(int i = 0; i < properties.length; i++)
            {
                if (properties[i] != null && !isInArray(i, thisSet))
                {
                    if (properties[i].getType() == Square.STANDARD_PROPERTY && ((StandardProperty)properties[i]).getDevelopment() > 1)
                    {
                        addition += (((StandardProperty)properties[i]).getDevelopment() - 1) / 2;
                    }
                    choices[count] = properties[i].toString() + " (" + (addition + properties[i].getPrice() / 2) + ")";
                    propertyLocs[count] = i;
                    propertyVals[count] = properties[i].getPrice() / 2;
                    count++;
                }
                addition = 0;
            }
            int choice = IO.prompt("Which property would you like to mortgage?", choices);
            Property thisProp = properties[propertyLocs[choice]];
            if (loseProperty(thisProp))
            {
                money += propertyVals[choice];
                numMortgagedProps++;
                mortgagedProps[thisProp.getPropertyNum()] = thisProp;
            }
            if(money >= 0)
            {
                return true;
            }
            else
            {
                return mortgage();
            }
        }
        else
        {
            return false;
        }
    }

    
    public boolean goBankrupt()
    {
        if(!mortgage())
        {
            bankruptcyProcedures();
            return true;
        }
        return false;
    }

    public void bankruptcyProcedures()
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
        if(getRemainingPlayers() == 1)
        {
            for (Player p: playerList)
            {
                if (p != null)
                {
                    IO.display(p.toString() + " has won the game!");
                    System.exit(0);
                }
            }
        }
        auctionAllStuff();
    }
    public void giveAllStuff(Player recipient)
    {
        IO.display(name + " gave all properties to " + recipient.toString());
        for (Property p: mortgagedProps)
        {
            if (p != null)
            {
                loseMortgagedProperty(p);
                recipient.gainMortgagedProperty(p);
            }
        }
    }

    public void auctionAllStuff()
    {
        IO.display(name + " has all their stuff up for auction.");
        for (Property p: mortgagedProps)
        {
            if (p != null)
            {
                resolveAuction(p, "Mortgaged");
            }
        }
    }


    public void gainProperty(Property prop)
    {
        properties[prop.getPropertyNum()] = prop;
        numProps++;
        if(prop.getType() == Square.STANDARD_PROPERTY)
        {
            int[] thisSet = Board.sets[((StandardProperty)prop).getSetNumber()];
            if(checkNulls(thisSet))
            {
                for (int i = 0; i < thisSet.length; i++)
                {
                    ((StandardProperty)properties[thisSet[i]]).develop(1);
                }
                IO.display(name  + ", you now have a set!");
            }
        }
        else if(prop.getType() == Square.RAILROAD_PROPERTY)
        {
            numRailroads++;
        }
        else
        {
            numUtilities++;
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
                int choice = IO.prompt("You will have to sacrifice your set and mortgage any developments on this property. Are you sure you want to do this?", choices);
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
        else if (prop.getType() == Square.RAILROAD_PROPERTY)
        {
            numRailroads--;
        }
        else
        {
            numUtilities--;
        }
        properties[prop.getPropertyNum()] = null;
        numProps--;
        return true;
    }

    public void gainMortgagedProperty(Property prop)
    {
        mortgagedProps[prop.getPropertyNum()] = prop;
        numMortgagedProps++;
    }

    public void loseMortgagedProperty(Property prop)
    {
        mortgagedProps[prop.getPropertyNum()] = null;
        numMortgagedProps--;
    }

    public void receive(int amount)
    {
        money += amount;
    }

    public boolean verifyPassword()
    {
        int count = 0;
        while (count < 3)
        {
            String attempt = IO.readString(name + ", please enter your password. Type cancel to cancel.");
            if (attempt.equals(password))
            {
                return true;
            }
            else if (attempt.equals("cancel"))
            {
                IO.display("Cancelled");
                return false;
            }
            else
            {
                IO.display("Invalid password. You have " +  (3 - ++count) + " attempts remaining");
            }
        }
        return false;
    }

    public String[] getGameData()
    {
        int numDataPoints = 0;
        for(Player p: playerList)
        {
            if (p != null)
            {
                numDataPoints += (13 + p.numProps + p.numMortgagedProps);
            }
        }
        String[] data = new String[numDataPoints];
        int i = 0;
        for (Player p: playerList)
        {
            if (p != null)
            {
                data[i++] = p.name;
                data[i++] = p.password;
                data[i++] = "" + p.position;
                data[i++] = "" + p.money;
                if (p.inJail)
                {
                    data[i++] = "1";
                }
                else
                {
                    data[i++] = "0";
                }
                data[i++] = "" + p.turnsInJail;
                data[i++] = "" + p.numRailroads;
                data[i++] = "" + p.numUtilities;
                data[i++] = "" + p.houseCount;
                data[i++] = "" + p.hotelCount;
                data[i++] = "" + p.getOutOfJailFree;
                data[i++] = "" + p.numProps;
                data[i++] = "" + p.numMortgagedProps;
                for (Property prop: p.properties)
                {
                    if (prop != null)
                    {
                        data[i++] = "" + prop.getPropertyNum();
                    }
                }
                for (Property prop: p.mortgagedProps)
                {
                    if (prop != null)
                    {
                        data[i++] = "" + prop.getPropertyNum();
                    }
                }
            }
        }
        return data;
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

    public Property[] getMortgagedProperties()
    {
        return mortgagedProps;
    }

    public int getNumRailroads()
    {
        return numRailroads;
    }
    public int getNumUtilities()
    {
        return numUtilities;
    }

    public String toString()
    {
        return name;
    }

    public boolean isLastPlayer()
    {
        for (int i = playerList.length - 1; i >= 0; i--)
        {
            if (playerList[i] != null)
            {
                if (playerList[i] == this)
                {
                    return true;
                }
                else 
                {
                    return false;
                }
            }
        }
        return false;
    }

    public void printProperties()
    {
        int count = 0;
        int owner;
        IO.display("");
        IO.display("Here is a list of all the properties:");
        for (Square s: Board.squareList)
        {
            if (s == Board.squareList[Board.propertyLocs[count]])
            {
                count++;
                owner = getOwner((Property)s);
                if(owner == -9)
                {
                    IO.display(s.toString());
                }
                else
                {
                    if (owner < 0)
                    {
                        IO.display(s.toString() + " (Mortgaged by " + playerList[Math.abs(owner) - 1] + ")");
                    }
                    else
                    {
                        IO.display(s.toString() + " (Owned by " + playerList[owner] + ")");
                    }
                }
            }
        }
        finishTurn();
    }
                
    public boolean isInArray(int val, int[] array)
    {
        for (int i: array)
        {
            if (i == val)
            {
                return true;
            }
        }
        return false;
    }
}
