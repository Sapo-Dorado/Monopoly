/**
 * This class is a subclass of Square and the superclass to RailroadProperty,
 * StandardProperty, and UtilityProperty. It represents a property on the 
 * Monopoly board.
 */
public class Property extends Square
{
    static int propertyCount = 0;

    private int price;
    private int propertyNum;
    
    /**
     * This is the constructor for a Property object.
     * 
     * @param name the name of the Property
     * @param type the type of the Square
     * @param propertyNum the number of the property with the first one after
     * go being one.
     */
    public Property(String name, int type, int price, int propertyNum)
    {
        super(name, type);
        this.price = price;
        this.propertyNum = propertyNum;
    }
    
    /**
     * This function returns the price of the Property.
     * 
     * @return the price of the Property
     */
    public int getPrice()
    {
        return price;
    }
    
    /**
     * This function returns the number of the Property
     * 
     * @return the number of the property with the first one after go being
     * one.
     */
    public int getPropertyNum()
    {
        return propertyNum;
    }
}

