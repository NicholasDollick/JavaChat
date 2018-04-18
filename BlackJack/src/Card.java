public class Card 
{
	private String type, suit;
	private int value;
	
	public Card(String suit, String type, int value)
	{
		this.suit = suit;
		this.type = type;
		this.value = value;
	}
	
	public int getCardValue()
	{
		return value;
	}
	
	public String toString()
	{
		return (type + " of " + suit);
	}
	
}
