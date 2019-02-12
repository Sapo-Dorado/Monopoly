public class UtilityProperty extends Property
{
    public UtilityProperty(String name)
    {
        super(name, Square.UTILITY_PROPERTY, 150,  Property.propertyCount);
        Property.propertyCount++;
    }
}
