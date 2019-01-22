public class Square
{
    static final int STANDARD_PROPERTY = 0;
    static final int RAILROAD_PROPERTY = 1;
    static final int UTILITY_PROPERTY = 2;
    static final int DECK = 3;
    static final int TAX = 4;
    static final int CORNER = 5;

    protected String name;
    protected int type;

    public String toString()
    {
        return name;
    }
    
    public int getType()
    {
        return type;
    }
}
    
