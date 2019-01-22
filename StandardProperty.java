public class StandardProperty extends Property
{
    private int buildingCost;
    private int[] costs;
    private int development;
    private int setNumber;
    
    public StandardProperty(String name, int price, int rent,
            int house1, int house2,
            int house3, int house4, int hotel,
            int buildingCost, int setNumber)
    {
        this.name = name;
        this.price = price;
        this.type = Square.STANDARD_PROPERTY;
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
        this.propertyNum = Property.propertyCount;
        Property.propertyCount++;
    }

    public void develop(int amount)
    {
        development += amount;
    }
    
    public void resetDevelopment()
    {
        development = 0;
    }

    public int getCost()
    {
        return costs[development];
    }

    public int getSetNumber()
    {
        return setNumber;
    }

    public int getDevelopment()
    {
        return development;
    }

    public int getBuildingCost()
    {
        return buildingCost;
    }
}
