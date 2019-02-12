public class RailroadProperty extends Property
{
    public RailroadProperty(String name)
    {
        super(name, Square.RAILROAD_PROPERTY, 200, Property.propertyCount);
        Property.propertyCount++;
    }
}
