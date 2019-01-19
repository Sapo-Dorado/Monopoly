public class Board
{
    private String[] squareList;
    private Property[] propertyList;
    static final int NUM_SQUARES = SquaresInfo.squareList.length;
    public Board()
    {
        squareList = SquaresInfo.squareList;
        propertyList = SquaresInfo.propertyList;
    }
    
    public String[] getSquareList()
    {
        return squareList;
    }
    
    public Property[] getPropertyList()
    {
        return propertyList;
    }
}
