public class Test
{
    public void tester(int[] var)
    {
    }

    public static void main(String[] args)
    {
        Player me = new Player("Nicholas", "hi");
        Player you = new Player("Bob", "ho");
        me.getOutOfJailFree = 3;
        me.evaluateSquare(1);
        you.printStatus();
    }
}
