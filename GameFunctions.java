public class GameFunctions
{
    public static void move(Player person, Board board)
    {
        int roll = person.move();
        System.out.println(person.getName() +
                " rolled a " + roll + ".");
        System.out.println(person.getName() +
                " is on square " +
                board.getSquareList()[person.getPosition()]);
        Property square = board.getPropertyList()[person.getPosition()];
        if (square != null)
        {
            System.out.println("The cost is " + square.getPrice() + ".");
        }
    }
}
