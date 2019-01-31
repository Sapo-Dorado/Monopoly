public class Test
{
    public static void main(String[] args)
    {
        Player me = new Player("Nicholas", "Gherkin");
        Player you = new Player("lol", "o");
        Player hi = new Player("Taco", "tuesday");
        me.money = 750;
        me.position = 37;
        me.evaluateSquare(2);
        me.position = 39;
        me.evaluateSquare(2);
        me.finishTurn();
        me.position = 3;
        me.evaluateSquare(2);
        me.printProperties();
    }
}
