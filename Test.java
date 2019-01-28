import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;


public class Test
{
    public static void main(String[] args)
    {
        try
        {
            Scanner in = new Scanner(new File("./gameSaves/test.txt"));
            System.out.println(in.nextLine());
        }
        catch (FileNotFoundException ex)
        {
        }
    }
}
