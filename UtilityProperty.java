public class UtilityProperty extends Property
{
    public UtilityProperty(String name)
    {
        this.name = name;
        this.price = 150;
        this.type = Square.UTILITY_PROPERTY;
        this.propertyNum = Property.propertyCount;
        Property.propertyCount++;
    }
}
