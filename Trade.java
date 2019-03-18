/**
 * This class represents a trade between two different players.
 */
public class Trade
{
    private int money;
    private Property[] props;
    private Property[] mortgagedProps;
    private int numGetOutOfJail;
    private int numPropsInTrade;
    private boolean isCancelled;
    private boolean hasProps;
    private boolean hasMortgagedProps;
    
    /**
     * This function constructs a trade with nothing inside of it.
     */
    public Trade()
    {
        this.money = 0;
        this.props = new Property[28];
        this.mortgagedProps = new Property[28];
        this.numGetOutOfJail = 0;
        this.numPropsInTrade = 0;
        this.isCancelled = false;
        this.hasProps = false;
        this.hasMortgagedProps = false;
    }

    /**
     * This function cancels the trade.
     */
    public void cancel()
    {
        isCancelled = true;
    }

    /**
     * This function adds a property to the trade.
     * 
     * @param prop the property to be added.
     */
    public void add(Property prop)
    {
        props[prop.getPropertyNum()] = prop;
        hasProps = true;
        numPropsInTrade++;
    }

    /**
     * This function adds a mortgaged Property to the trade.
     * 
     * @param prop the mortgaged property to be added.
     */
    public void addMortgaged(Property prop)
    {
        mortgagedProps[prop.getPropertyNum()] = prop;
        hasMortgagedProps = true;
        numPropsInTrade++;
    }

    /**
     * This function adds money to a trade.
     * 
     * @param the amount of money to be added.
     */
    public void addMoney(int amount)
    {
        money += amount;
        if (money < 0)
        {
            IO.display("Money is less than $0. Resetting money to $0");
            money = 0;
        }
    }
    
    /**
     * This function adds a get out of jail free card to the trade.
     */
    public void addGetOutOfJailFree()
    {
        numGetOutOfJail++;
    }

    /**
     * This function checks to see if the trade includes a specific property.
     * @param prop the property being checked.
     */
    public boolean includesProp(Property prop)
    {
        return props[prop.getPropertyNum()] != null;
    }

    /**
     * This function checks to see if the trade includes a specific mortgaged
     * property.
     * @param prop the mortgaged property to be checked.
     */
    public boolean includesMortgagedProp(Property prop)
    {
        return mortgagedProps[prop.getPropertyNum()] != null;
    }

    public int getMoney()
    {
        return money;
    }

    public Property[] getProps()
    {
        return props;
    }

    public Property[] getMortgagedProps()
    {
        return mortgagedProps;
    }
    
    public int getNumJail()
    {
        return numGetOutOfJail;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public String toString()
    {
        String output = "";
        output += "This trade includes:\n";
        output += "Money : " + money + "\n";
        output += ("Properties:\n");
        for(Property p: props)
        {
            if (p != null)
            {
                output += p.toString() + "\n";
            }
        }
        for (Property p: mortgagedProps)
        {
            if (p != null)
            {
                output += p.toString() + " (Mortgaged)\n";
            }
        }
        output += "Get Out of Jail Free Cards: " + numGetOutOfJail;
        return output;
   }
   
   public boolean isEmpty()
   {
       return money==0 && numGetOutOfJail == 0 && !hasProps && !hasMortgagedProps;
   }

   public int getNumProps()
   {
       return numPropsInTrade;
   }
}
