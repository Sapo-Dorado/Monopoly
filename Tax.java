public class Tax extends Square
{
    private int fee;
    public Tax(String name, int fee)
    {
        super(name, Square.TAX);
        this.fee = fee;
    }

    public int getFee()
    {
        return fee;
    }
}
