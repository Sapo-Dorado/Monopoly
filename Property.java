public class Property extends Square
{
    static int propertyCount = 0;

    protected int price;
    protected int propertyNum;
    
    public Property(String name, int type, int price, int propertyNum)
    {
        super(name, type);
        this.price = price;
        this.propertyNum = propertyNum;
    }
    
    public int getPrice()
    {
        return price;
    }
    

    public int getPropertyNum()
    {
        return propertyNum;
    }
}

