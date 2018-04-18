import java.io.*;
import java.security.SecureRandom;


//@SuppressWarnings("unused") //for upcoming changes
public class DeckOfCards 
{
	private Card[] deck;
	private int currentCard;
	
	public DeckOfCards() throws IOException
	{
		String[] faces = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
		int[] cardVal = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
		String[] suits = {"clubs", "spades", "hearts", "diamonds"};
		
		deck = new Card[52];
		currentCard = 0;
		
		for(int suit = 0; suit < suits.length; suit++)
		{
			for(int face = 0; face < faces.length; face++)
			{
				deck[(face + (suit * 13))] = new Card(suits[suit], faces[face], cardVal[face]);
			}
		}
	}
	
	public void shuffle()
	{
		currentCard = 0;
		
		SecureRandom rand = new SecureRandom();
		
		for(int i = 0; i < deck.length; i++)
		{
			int j = rand.nextInt(52);
			
			Card temp = deck[i];
			deck[i] = deck[j];
			deck[j] = temp;
		}
	}
	
	public Card dealCard()
	{
		if(currentCard < deck.length)
			return deck[currentCard++];
		else
			return null;
	}
	
	public void displayDeck()
	{
		for(Card card : deck)
		{
			System.out.println(card + " " + card.getCardValue());
		}
	}
	
	public void displayDeckValues()
	{
		for(Card card : deck)
		{
			System.out.println(card.getCardValue());
		}
	}
	

}
