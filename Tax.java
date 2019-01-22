public class Tax extends Square
{
    private int fee;
    public Tax(String name, int fee)
    {
        this.name = name;
        this.fee = fee;
        this.type = Square.TAX;
    }

    public int getFee()
    {
        return fee;
    }
}
