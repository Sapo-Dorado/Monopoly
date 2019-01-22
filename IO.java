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
}
