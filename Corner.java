/**
 * This class represents the corners of the Monopoly board.
 */
public class Corner extends Square
{
    /**
     * This is the constructor for a corner object. It creates a square with a name and the type
     * CORNER.
     * 
     * @param name a string which is the name of the corner.
     */
    public Corner(String name)
    {
        super(name, Square.CORNER);
    }
}
