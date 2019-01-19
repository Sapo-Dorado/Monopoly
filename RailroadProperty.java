public class RailroadProperty extends Property
{
    public RailroadProperty(String name)
    {
        this.name = name;
        this.price = 200;
        costs[0] = 25;
        costs[1] = 50;
        costs[2] = 100;
        costs[3] = 200;
        development = 0;
    }
}
