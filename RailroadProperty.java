public class RailroadProperty extends Property
{
    public RailroadProperty(String name)
    {
        this.name = name;
        this.price = 200;
        this.type = Square.RAILROAD_PROPERTY;
        this.propertyNum = Property.propertyCount;
        Property.propertyCount++;
    }
}
