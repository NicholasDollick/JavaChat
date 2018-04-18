import java.io.IOException;
import java.util.ArrayList;

public class Trainer 
{
	 private static int[][] valueTable = new int[][]{
		//{"void", "2", "3", "4", "5", "6",  "7", "8", "9", "10", "jack", "queen", "king", "ace"},
		 
		{-128, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
		{  17, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0},
		{  16, 0, 0, 0, 0, 0, 1, 1, 1,  1,  1},
		{  15, 0, 0, 0, 0, 0, 1, 1, 1,  1,  1},
		{  14, 0, 0, 0, 0, 0, 1, 1, 1,  1,  1},
		{  13, 0, 0, 0, 0, 0, 1, 1, 1,  1,  1},
		{  12, 1, 1, 0, 0, 0, 1, 1, 1,  1,  1},
		{  11, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1},
		{  10, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1},
		{   9, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1},
		{   8, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1},
		{   7, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1},
		{   6, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1},
		{   5, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1}
		};
	
		public static int getChartValue(int handVal, int dealerCard)
		{
			int move = 0;
			
			if(handVal > 17)
			{
				return move;
			}
			for(int i = 0; i < valueTable.length; i++)
			{
				if(valueTable[i][0] == handVal)
					for(int j = 0; j < valueTable[i].length; j++)
					{
						if(valueTable[0][j] == dealerCard)
						{
							move = valueTable[i][j];
						}
					}
			}
			
			return move;
		}
		
		/*
		 * This version of the method is used for AI logic during game
		 */
		public static void getMove(ArrayList<Card> player, Card dealer)
		{
			
			int move = getChartValue(GameNoGui.checkSum(player), dealer.getCardValue());
			int dealerValue = dealer.getCardValue();
			
			if(player.get(0).getCardValue() == (player.get(1).getCardValue()) && player.size() == 2)
			{
				int value = player.get(0).getCardValue();
				
				switch(value)
				{
					case(11):
						System.out.println("Split");
						break;
					
					case(10):
						System.out.println("Stand");
						break;
						
					case(9):
						if(dealerValue == 7 || dealerValue == 10 || dealerValue == 11)
							System.out.println("Stand");
						else
							System.out.println("Split");
						break;
						
					case(8):
						System.out.println("Split");
						break;
						
					case(7):
						if(dealerValue >= 8)
							System.out.println("Hit");
						else
							System.out.println("Split");
						break;
						
					case(6):
						if(dealerValue >= 7)
							System.out.println("Hit");
						else
							System.out.println("Split");
						break;
						
					case(5):
						System.out.println("Hit");
						break;
						
					case(4):
						if(dealerValue == 5 || dealerValue == 6)
							System.out.println("Split");
						else
							System.out.println("Hit");
						break;
						
					case(3):
						if(dealerValue >= 8)
							System.out.println("Hit");
						else
							System.out.println("Split");
						break;
						
					case(2):
						if(dealerValue >= 8)
							System.out.println("Hit");
						else
							System.out.println("Split");
						break;
				}
			}
			
			else if(player.size() == 2 && (player.get(0).getCardValue() == 11 || player.get(1).getCardValue() == 11))
			{
				int value = 0;
				
				//get value of the card that isn't the ace
				if(player.get(0).getCardValue() == 11)
					value = player.get(1).getCardValue();
				else
					value = player.get(0).getCardValue();
				
				
				switch(value)
				{
					case(10):
						System.out.println("Stand");
						break;
						
					case(9):
						System.out.println("Stand");
						break;
						
					case(8):
						System.out.println("Stand");
						break;
						
					case(7):
						if(dealerValue == 2 || dealerValue == 7 || dealerValue == 8)
							System.out.println("Stand");
						else
							System.out.println("Hit");
						break;
						
					case(6):
						System.out.println("Hit");
						break;
						
					case(5):
						System.out.println("Hit");
						break;
						
					case(4):
						System.out.println("Hit");
						break;
						
					case(3):
						System.out.println("Hit");
						break;
						
					case(2):
						System.out.println("Hit");
						break;
				}
			}
			else
			{	
				if(move == 1)
					System.out.println("Hit");
				else if(move == 0)
					System.out.println("Stand");
				else if(move < 0)
					System.out.println("Something went wrong. This should never display");
			}
		}
		
		/*
		 * This version is used by human players wanting ideal move 
		 */
		public static void getMove(int player, int dealer)
		{
			
		}
		
		
		public static void main(String [] args) throws IOException
		{
			DeckOfCards deck = new DeckOfCards();
			ArrayList<Card> hand = new ArrayList<>();
			ArrayList<Card> dhand = new ArrayList<>();
			deck.shuffle();
			deck.shuffle();
			
			hand.add(deck.dealCard());
			hand.add(deck.dealCard());
			dhand.add(deck.dealCard());
			System.out.println("Your Hand: " + GameNoGui.checkSum(hand));
			System.out.println("Dealer Hand: " + GameNoGui.checkSum(dhand));
			System.out.println();
			System.out.print("Your move should be: ");
			getMove(hand, dhand.get(0));
		}

}
