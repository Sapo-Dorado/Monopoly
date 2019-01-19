public class Driver
{
    public static void main(String[] args)
    {
        Board gameBoard = new Board();
        Player me = new Player("Nicholas");
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
        GameFunctions.move(me, gameBoard);
    }
}
