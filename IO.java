import java.util.Scanner;
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
}
