public class Test
{
    public void tester(int[] var)
    {
    }

    public static void main(String[] args)
    {
        Player me = new Player("Nicholas", "hi");
        Player you = new Player("Bob", "ho");
        me.position = 1;
        me.evaluateSquare(1);
        me.money = 20;
        me.forceSpend(60);
    }
}
