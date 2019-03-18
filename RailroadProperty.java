/**
 * This class represents Railroad Properties in Monopoly.
 */
public class RailroadProperty extends Property
{
    /**
     * This is the constructor for the Railroad Property. It only takes a name
     * because the other parameters for this type of property are consistent.
     * 
     * @param name the name of the Property
     */
    public RailroadProperty(String name)
    {
        super(name, Square.RAILROAD_PROPERTY, 200, Property.propertyCount);
        Property.propertyCount++;
    }
}
