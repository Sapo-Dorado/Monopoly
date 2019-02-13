/**
 * This class is the superclass to Property, CommunityChest, Chance, Corner, and Tax. It stores
 * a name and a type for the class and it contains some static variables to be used when checking
 * types.
 */
public class Square
{
    static final int STANDARD_PROPERTY = 0;
    static final int RAILROAD_PROPERTY = 1;
    static final int UTILITY_PROPERTY = 2;
    static final int DECK = 3;
    static final int TAX = 4;
    static final int CORNER = 5;

    private String name;
    private int type;
    
    /**
     * This is the constructor for the square object. It takes a name and a type.
     * 
     * @param name a string representing the name of the square
     * @param type an integer representing the type of the square
     */
    public Square(String name, int type)
    {
        this.name = name;
        this.type = type;
    }

    /**
     * This is the toString method for the square, and it just returns the square's name.
     * 
     * @return a string which is the name of the class
     */
    public String toString()
    {
        return name;
    }
    
    /**
     * This function returns the square's type.
     * 
     * @return an integer that indicates the type of the square
     */
    public int getType()
    {
        return type;
    }
}
    
