/**
 * This class represents Standard Properties in Monopoly.
 */
public class StandardProperty extends Property
{
    private int buildingCost;
    private int[] costs;
    private int development;
    private int setNumber;
    
    /**
     * This is the constructor for a Standard Property. It takes all the data
     * about that property as parameters.
     * 
     * @param name the name of the property
     * @param price the price of the property
     * @param rent the rent of the property
     * @param house1 the rent of the property after 1 house.
     * @param house2 the rent of the property after 2 house.
     * @param house3 the rent of the property after 3 house.
     * @param house4 the rent of the property after 4 house.
     * @param hotel the rent of the property after a hotel.
     * @param buildingCost the cost of building one house or one hotel.
     * @param setNumber a number representing what set this property is a part
     * of
     */
    public StandardProperty(String name, int price, int rent,
            int house1, int house2,
            int house3, int house4, int hotel,
            int buildingCost, int setNumber)
    {
        super(name, Square.STANDARD_PROPERTY,  price, Property.propertyCount);
        Property.propertyCount++;
        this.costs = new int[7];
        this.costs[0] = rent;
        this.costs[1] = rent * 2;
        this.costs[2] = house1;
        this.costs[3] = house2;
        this.costs[4] = house3;
        this.costs[5] = house4;
        this.costs[6] = hotel;
        this.buildingCost = buildingCost;
        this.development = 0;
        this.setNumber = setNumber;
    }

    /**
     * This function increases the development of the property by a given
     * amount.
     * 
     * @param amount how much the property is developed.
     */
    public void develop(int amount)
    {
        development += amount;
    }
    
    /**
     * This function resets the development of the property to 0.
     */
    public void resetDevelopment()
    {
        development = 0;
    }

    /**
     * This function returns the current rent of this property.
     * 
     * @return the rent
     */
    public int getCost()
    {
        return costs[development];
    }

    /**
     * This function returns the set number of this property.
     * 
     * @return the set number
     */
    public int getSetNumber()
    {
        return setNumber;
    }

    /**
     * This function returns the development level of this property.
     * 
     * @return the development level.
     */
    public int getDevelopment()
    {
        return development;
    }

    /**
     * This function returns the cost of a building for this property.
     * 
     * @return the building cost
     */
    public int getBuildingCost()
    {
        return buildingCost;
    }
}
