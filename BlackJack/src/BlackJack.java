import java.io.IOException;
import java.util.Scanner;

public class BlackJack {

	public static void main(String[] args) throws IOException 
	{
		Scanner input = new Scanner(System.in);
		String in = "";
		System.out.println("**** Welcome To Nicks Blackjack ****");
		while(1<2)
		{
			GameNoGui.play();
			System.out.println("\n");
			//System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n \n"); //full screen wipe
			System.out.print("Play Again(Y/N): ");
			in = validate(input.nextLine().toLowerCase());
			if(in.equals("y"))
			{
				System.out.println("\n");
				continue;
			}
			else
			{
				System.out.println("Thanks for playing!");
				input.close();
				System.exit(1);
			}
		}
	}
	
	@SuppressWarnings("resource")
	public static String validate(String input)
	{
		String out = "";
		Scanner scan = new Scanner(System.in);
		if(input.equals("y") || input.equals("n"))
			out = input;
		else
		{
			System.out.print("Play Again(Y/N): ");
			out = validate(scan.nextLine().toLowerCase());
		}
		
		return out;
	}
}
