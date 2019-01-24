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

    public void cancel()
    {
        isCancelled = true;
    }

    public void add(Property prop)
    {
        props[prop.getPropertyNum()] = prop;
        hasProps = true;
        numPropsInTrade++;
    }

    public void addMortgaged(Property prop)
    {
        mortgagedProps[prop.getPropertyNum()] = prop;
        hasMortgagedProps = true;
        numPropsInTrade++;
    }

    public void addMoney(int amount)
    {
        money += amount;
        if (money < 0)
        {
            IO.display("Money is less than $0. Resetting money to $0");
            money = 0;
        }
    }
    
    public void addGetOutOfJailFree()
    {
        numGetOutOfJail++;
    }

    public boolean includesProp(Property prop)
    {
        return props[prop.getPropertyNum()] != null;
    }

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
