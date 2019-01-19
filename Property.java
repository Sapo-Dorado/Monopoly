public class Property
{
    protected String name;
    protected int price;
    protected int[] costs;
    protected int development;
    public String getName()
    {
        return name;
    }
    
    public int getPrice()
    {
        return price;
    }
    
    public int getCost()
    {
        return costs[development];
    }
}

