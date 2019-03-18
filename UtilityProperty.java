/**
 * This class represents Utility Properties in Monopoly.
 */
public class UtilityProperty extends Property
{
    /**
     * This is the constructor of the Utility Property. It only takes a name 
     * because the rest of the parameters of this type of Property are
     * consistent.
     * 
     * @param name the name of the property.
     */
    public UtilityProperty(String name)
    {
        super(name, Square.UTILITY_PROPERTY, 150,  Property.propertyCount);
        Property.propertyCount++;
    }
}
