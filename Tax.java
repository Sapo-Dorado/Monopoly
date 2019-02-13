/**
 * This class represents tax squares on a monopoly board. They each have a fee associated with them.
 */
public class Tax extends Square
{
    private int fee;
    
    /**
     * This is the constructor of a tax square. It takes a fee and a name.
     * 
     * @param name a string which is the name of the tax square.
     * @param fee an integer which is the fee for that square.
     */
    public Tax(String name, int fee)
    {
        super(name, Square.TAX);
        this.fee = fee;
    }

    /**
     * This method returns the fee for the tax square.
     * 
     * @return an integer which is the fee for that square.
     */
    public int getFee()
    {
        return fee;
    }
}
