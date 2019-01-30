public class Test
{
    public static void main(String[] args)
    {
        Player me = new Player("Nicholas", "Gherkin");
        me.money = 410;
        me.position = 3;
        me.evaluateSquare(2);
        me.position = 37;
        me.evaluateSquare(2);
        me.position = 39;
        me.evaluateSquare(2);
    }
}
