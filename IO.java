import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class IO
{
    public static void display(String message)
    {
        System.out.println(message);
    }

    public static int prompt(String message, String[] choices)
    {
        Scanner in = new Scanner(System.in);
        System.out.println(message);
        for (int i = 0; i < choices.length; i++)
        {
            System.out.println(i + ". " + choices[i]);
        }
        if(in.hasNextInt())
        {
            int input = in.nextInt();
            if (input >= 0 && input < choices.length)
            {
                return input;
            }
        }
        System.out.println("Invalid input");
        return prompt(message, choices);
    }

    public static int getOffer(String message, int startingBid)
    {
        Scanner in = new Scanner(System.in);
        System.out.println(message);
        if (in.hasNextInt())
        {
            int val = in.nextInt();
            if (val > startingBid)
            {
                return val;
            }
        }
        System.out.println("Invalid bid");
        return getOffer(message, startingBid);
    }

    public static int getMoney()
    {
        Scanner in = new Scanner(System.in);
        if (in.hasNextInt())
        {
            return in.nextInt();
        }
        else
        {
            System.out.println("Invalid input");
            return getMoney();
        }
    }

    public static String readString(String prompt)
    {
        Scanner in = new Scanner(System.in);
        System.out.println(prompt);
        if (in.hasNext())
        {
            return in.nextLine();
        }
        else
        {
            return readString(prompt);
        }
    }
    
    public static int getAmount(String prompt)
    {
        Scanner in = new Scanner(System.in);
        System.out.println(prompt);
        if (in.hasNextInt())
        {
            return in.nextInt();
        }
        else
        {
            return getAmount(prompt);
        }
    }

    public static void saveGame(String saveName, String[] data)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./gameSaves/" + saveName + ".txt"))))
        {
            for (String line: data)
            {
               writer.write(line);
               writer.newLine();
            }
            System.out.println("File successfully saved");
            writer.close();
            System.exit(0);
        }
        catch(IOException e)
        {
            System.out.println("Problem writing to file");
        }
    }

    public static String[] readSave(String saveName)
    {
        String[] data;
        try
        {
            Scanner preliminaryScan = new Scanner(new File("./gameSaves/" + saveName + ".txt"));
            int numLines = 0;
            while (preliminaryScan.hasNextLine())
            {
                numLines++;
                preliminaryScan.nextLine();
            }
            Scanner read = new Scanner(new File("./gameSaves/" + saveName + ".txt"));
            data = new String[numLines];
            for (int i = 0; i < numLines; i++)
            {
                data[i] = read.nextLine();
            }
            return data;
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Save not found");
            System.exit(0);
        }
        data = new String[1];
        return data;
    }
}
