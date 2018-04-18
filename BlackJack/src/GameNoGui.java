import java.util.*;
import java.io.*;

public class GameNoGui 
{
	public static void play() throws IOException
	{	
		DeckOfCards deck = new DeckOfCards();
		ArrayList<Card> dealerHand = new ArrayList<Card>(), playerHand = new ArrayList<Card>();
		boolean blackjack = false;
		
		deck.shuffle(); //deck initializes in order, that wouldn't make for a very fun game
		deck.shuffle(); //shuffle twice to get an extreme shuffle
		
		//deal initial hands
		dealerHand.add(deck.dealCard());
		playerHand.add(deck.dealCard());
		dealerHand.add(deck.dealCard());
		playerHand.add(deck.dealCard());
		
		//display your hand, and first card from dealer
		System.out.println("Your hand: " + playerHand.toString());
		System.out.println("Dealer hand: [" + dealerHand.get(0).toString() + ", HIDDEN]");
		
		if(checkSum(playerHand) == 21)
		{
			System.out.println("[+] BlackJack!");
			blackjack = true;
		}
		
		while(!blackjack)
		{
			getAction(deck, playerHand, dealerHand, true);
			break;
		}
	}
	
	
	@SuppressWarnings("resource")
	public static void getAction(DeckOfCards deck, ArrayList<Card> playerHand, ArrayList<Card> dealerHand, boolean endOfTurn)
	{ 
		Scanner input = new Scanner(System.in);
		String action = "";
		
		System.out.print("Hit or Stand: ");
		action = validate(input.nextLine().toLowerCase());
		if(action.equals("hit") || action.equals("h"))
        {
            playerHand.add(deck.dealCard());
            System.out.println("Your hand: " + playerHand.toString());
            if(!checkValue(playerHand))
            {
                System.out.println("Bust!");
                return;
            }
            else
            	getAction(deck, playerHand, dealerHand, endOfTurn);
        }
		 if(action.equals("stand") || action.equals("s"))
         {
             if(endOfTurn)
             {
            	 System.out.println("Dealer hand: " + dealerHand.toString());
            	 while(checkSum(dealerHand) < 17)
            	 {
            		 dealerHand.add(deck.dealCard());
            		 System.out.println("Dealer hand: " + dealerHand.toString());
            	 }
            	 if(checkValue(dealerHand))
            	 {
            		 checkValue(dealerHand, playerHand);
            		 return;
            	 }
            	 else
            	 {
            		 System.out.println("[+] You Win!");
            		 return;
            	 }
             }
             else
            	 return;
         }
	      if(action.equals("split")) //This entire option is a bit of a mess
          {
              ArrayList<Card> playerHand2 = new ArrayList<Card>();
              boolean blackjackA = false, blackjackB = false;
              endOfTurn = false;
              
              System.out.println("[*] Splitting Hand");
              playerHand2.add(playerHand.get(1));
              playerHand.remove(1);
              playerHand.add(deck.dealCard());
              playerHand2.add(deck.dealCard());
              System.out.println("Hand A: " + playerHand.toString());
              System.out.println("Hand B: " + playerHand2.toString());
      		if(checkSum(playerHand) == 21)
    		{
    			System.out.println("[+] BlackJack!");
    			blackjackA = true;
    		}
      		if(checkSum(playerHand2) == 21)
    		{
    			System.out.println("[+] BlackJack!");
    			blackjackB = true;
    		}
              System.out.println("[*] For Hand A");
              while(!blackjackA)
              {
            	  getAction(deck, playerHand, dealerHand, endOfTurn);
            	  System.out.println();
            	  break;
              }
              System.out.println("[*] For Hand B");
              while(!blackjackB)
              {
            	  getAction(deck, playerHand2, dealerHand, endOfTurn);
            	  System.out.println();
            	  break;
              }

              endOfTurn(deck, playerHand, dealerHand);

              endOfTurn(deck, playerHand2, dealerHand);
              
          }
	}
	
	public static boolean checkValue(ArrayList<Card> hand)
	{
		boolean gameFlag = true;
		
		if(checkSum(hand) > 21)
			gameFlag = false;
		
		return gameFlag;
	}
	
	public static void checkValue(ArrayList<Card> dealer, ArrayList<Card> player)
	{	
		if(checkSum(dealer) < checkSum(player))
			System.out.println("[+] You Win!");
		else if(checkSum(dealer) == checkSum(player))
			System.out.println("[-] Push");
		else
			System.out.println("[-] Dealer Wins!");	
	}
	
	public static int checkSum(ArrayList<Card> hand)
	{
		int sum = 0;
		
		for(Card card : hand)
			sum += card.getCardValue();
		
		return sum;
	}
	
	public static void endOfTurn(DeckOfCards deck, ArrayList<Card> playerHand, ArrayList<Card> dealerHand)
	{
    	 while(checkSum(dealerHand) < 17)
    	 {
    		 dealerHand.add(deck.dealCard());
    		 System.out.println("Dealer hand: " + dealerHand.toString());
    	 }
    	 if(checkValue(dealerHand))
    	 {
    		 checkValue(dealerHand, playerHand);
    		 return;
    	 }
    	 else
    	 {
    		 System.out.println("[+] You Win!");
    		 return;
    	 }

	}
	
	/*
	 * Each ace is treated as 11 by default.
	 * When the hand value is over 21, subtract 10 
	 * from the total for each ace in hand.
	 */
	public static int handleAce(ArrayList<Card> playerHand)
	{
		int handValue = checkSum(playerHand);
		
		for(Card card : playerHand)
		{
			if(card.getCardValue() == 11)
			{
				if(checkSum(playerHand) > 21)
				{
					handValue -= 10;
				}
			}
		}
		
		return handValue;
	}
	
	@SuppressWarnings("resource")
	public static String validate(String input)
	{
		String out = "";
		Scanner scan = new Scanner(System.in);
		if(input.equals("hit") || input.equals("h") || input.equals("stand") || input.equals("s") || input.equals("split"))
			out = input;
		else
		{
			System.out.print("Hit or Stand: ");
			out = validate(scan.nextLine().toLowerCase());
		}
		return out;
	}
	
	
}
