import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
public class Test
{
    public static void main(String[] args)
    {
        String saveName = "HI";
        String[] data = {"hi", "hi"};
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./gameSaves/" + saveName + ".txt")))))
        {
            for (String line: data)
            {
               writer.write(line + "\n");
            }
            System.out.println("File successfully saved");
            System.exit(0);
        }
        catch(IOException e)
        {
            System.out.println("Problem writing to file");
        }
    }
}
