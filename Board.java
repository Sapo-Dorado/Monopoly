public class Board
{
    static final int NUM_SQUARES = 40;
    static final int NUM_PROPERTIES = 28;
    static final int[] brown = {0, 1};
    static final int[] lightBlue = {3, 4, 5};
    static final int[] purple = {6, 8, 9};
    static final int[] orange = {11, 12, 13};
    static final int[] red = {14, 15, 16};
    static final int[] yellow = {18, 19, 21};
    static final int[] green = {22, 23, 24};
    static final int[] blue = {26, 27};
    static final int[][] sets = {brown, lightBlue, purple, orange, red, yellow, green, blue};
    static final Square[] squareList = {
            (new Corner("Go")),
            (new StandardProperty("Mediterranean Avenue", 60, 2, 10, 30, 90, 160, 250, 50, 0)),
            (new CommunityChest()),
            (new StandardProperty("Baltic Avenue", 60, 4, 20, 60, 180, 320, 450, 50, 0)),
            (new Tax("Income Tax", 200)),
            (new RailroadProperty("Reading Railroad")),
            (new StandardProperty("Oriental Avenue", 100, 6, 30, 90, 270, 400, 550, 50, 1)),
            (new Chance()),
            (new StandardProperty("Vermont Avenue", 100, 6, 30, 90, 270, 400, 550, 50, 1)),
            (new StandardProperty("Connecticut Avenue", 120, 8, 40, 100, 300, 450, 600, 50, 1)),
            (new Corner("Jail")),
            (new StandardProperty("St. Charles Place", 140, 10, 50, 150, 450, 625, 750, 100, 2)),
            (new UtilityProperty("Electric Company")),
            (new StandardProperty("States Avenue", 140, 10, 50, 150, 450, 625, 750, 100, 2)),
            (new StandardProperty("Virginia Avenue", 160, 12, 60, 180, 500, 700, 900, 100, 2)),
            (new RailroadProperty("Pensylvania Railroad")),
            (new StandardProperty("St. James Place", 180, 14, 70, 200, 550, 750, 950, 100, 3)),
            (new CommunityChest()),
            (new StandardProperty("Tennessee Avenue",180, 14, 70, 200, 550, 750, 950, 100, 3)),
            (new StandardProperty("New York Avenue", 200, 16, 80, 220, 600, 800, 1000, 100, 3)),
            (new Corner("Free Parking")),
            (new StandardProperty("Kentucky Avenue", 220, 18, 90, 250, 700, 875, 1050, 100, 4)),
            (new Chance()),
            (new StandardProperty("Indiana Avenue", 220, 18, 90, 250, 700, 875, 1050, 100, 4)),
            (new StandardProperty("Illinois Avenue", 240, 20, 100, 300, 750, 925, 1100, 150, 4)),
            (new RailroadProperty("B. & O. Railroad")),
            (new StandardProperty("Atlantic Avenue", 260, 22, 110, 330, 800, 975, 1150, 150, 5)),
            (new StandardProperty("Ventnor Avenue", 260, 22, 110, 330, 800, 975, 1150, 150, 5)),
            (new UtilityProperty("Water Works")),
            (new StandardProperty("Marvin Gardens", 280, 24, 120, 350, 850, 1025, 1200, 150, 5)),
            (new Corner("Go to Jail")),
            (new StandardProperty("Pacific Avenue", 300, 26, 130, 390, 900, 1100, 1275, 150, 6)),
            (new StandardProperty("North Carolina Avenue", 300, 26, 130, 390, 900, 1100, 1275, 150, 6)),
            (new CommunityChest()),
            (new StandardProperty("Pennsylvania Avenue", 320, 28, 150, 450, 1000, 1200, 1400, 200, 6)),
            (new RailroadProperty("Short Line")),
            (new Chance()),
            (new StandardProperty("Park Place", 350, 35, 175, 500, 1100, 1300, 1500, 200, 7)),
            (new Tax("Luxury Tax", 100)),
            (new StandardProperty("Boardwalk", 400, 50, 200, 600, 1400, 1700, 2000, 200, 7)),
          };
    public static int[] propertyLocs = {1,3,5,6,8,9,11,12,13,14,15,16,18,19,21,23,24,25,26,27,28,29,
        31,32,34,35,37,39};
    public static void printBoard()
    {
        IO.display("|--------------------------------------------------------------------------------------------------------------------------------------------------|");
        IO.display("|              |  Kentucky  |            |  Indiana   |  Illinois  |     B&O    |  Atlantic  |   Ventnor  |    Water   |   Marvin   |    Go to     |");
        IO.display("| Free Parking |   Avenue   |   Chance   |   Avenue   |   Avenue   |   Railroad |   Avenue   |   Avenue   |    Works   |   Gardens  |     Jail     |");
        IO.display("|              | Price: 220 |            | Price: 220 | Price: 240 | Price: 200 | Price: 260 | Price: 260 | Price: 150 | Price: 280 |              |");
        IO.display("|--------------|-----------------------------------------------------------------------------------------------------------------------------------|");
        IO.display("|   New York   |                                                                                                                    |   Pacific    |");
        IO.display("|    Avenue    |                                                                                                                    |    Avenue    |");
        IO.display("|  Price: 200  |                                                                                                                    |  Price: 300  |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|  Tennessee   |                                                                                                                    |North Carolina|");
        IO.display("|    Avenue    |                                                                                                                    |    Avenue    |");
        IO.display("|  Price: 180  |                                                                                                                    |  Price: 300  |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|  Community   |                                                                                                                    |  Community   |");
        IO.display("|    Chest     |                                                                                                                    |    Chest     |");
        IO.display("|              |                                                                                                                    |              |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|  St. James   |                                                                                                                    | Pennsylvania |");
        IO.display("|    Place     |                                                                                                                    |    Avenue    |");
        IO.display("|  Price: 180  |                                                                                                                    |  Price: 320  |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("| Pennsylvania |                                                                                                                    |    Short     |");
        IO.display("|   Railroad   |                                                                                                                    |    Line      |");
        IO.display("|  Price: 200  |                                                                                                                    |  Price: 200  |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|   Virginia   |                                                                                                                    |              |");
        IO.display("|    Avenue    |                                                                                                                    |    Chance    |");
        IO.display("|  Price: 160  |                                                                                                                    |              |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|    States    |                                                                                                                    |    Park      |");
        IO.display("|    Avenue    |                                                                                                                    |    Place     |");
        IO.display("|  Price: 140  |                                                                                                                    |  Price: 350  |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|   Electric   |                                                                                                                    |    Luxury    |");
        IO.display("|   Company    |                                                                                                                    |     Tax      |");
        IO.display("|  Price: 150  |                                                                                                                    |   Pay: 100   |");
        IO.display("|--------------|                                                                                                                    |--------------|");
        IO.display("|  St. Charles |                                                                                                                    |  Boardwalk   |");
        IO.display("|     Place    |                                                                                                                    |              |");
        IO.display("|  Price: 140  |                                                                                                                    |  Price: 400  |");
        IO.display("|--------------|--------------------------------------------------------------------------------------------------------------------|--------------|");
        IO.display("|   In Jail/   | Connecticut|   Vermont  |            |  Oriental  |  Reading   |   Income   |   Baltic   |  Community |Mediterran- |              |");
        IO.display("| Just Visiting|   Avenue   |   Avenue   |   Chance   |   Avenue   |  Railroad  |     Tax    |   Avenue   |    Chest   | ean Avenue |     GO       |");
        IO.display("|              | Price: 120 | Price: 100 |            | Price: 100 | Price: 200 |  Pay: 200  | Price: 60  |            | Price: 60  |              |");
        IO.display("|--------------------------------------------------------------------------------------------------------------------------------------------------|");
    }
}
