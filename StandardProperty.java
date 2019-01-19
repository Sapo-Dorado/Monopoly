public class StandardProperty extends Property
{
    private int buildingCost;

    public StandardProperty(String name, int price, int rent,
            int house1, int house2,
            int house3, int house4, int hotel,
            int buildingCost)
    {
        this.name = name;
        this.price = price;
        this.costs[0] = rent;
        this.costs[1] = rent * 2;
        this.costs[2] = house1;
        this.costs[3] = house2;
        this.costs[4] = house3;
        this.costs[5] = house4;
        this.costs[6] = hotel;
        this.buildingCost = buildingCost;
        this.development = 0;
    }
}
