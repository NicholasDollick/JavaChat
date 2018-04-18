import javax.swing.JOptionPane;
public class Node 
{
	String item;
	static String search;
	Node next;
	
	Node(String s)
	{
		item = s;
		next = null; 
	}
	
	Node(String s, Node n)
	{
		this.item = s;
		this.next = n;
	}
	public static void main(String[] args) 
	{
		Node h = new Node("bill");
		Node x = h;
		x.next = new Node("fred");
		
		x = x.next;
		x.next = new Node("jane");
		
		h = new Node("al", h); //adds to beginning, becomes new  head
		
		displayList(h);
		System.out.println();
		
		x = h;					//adds item to end of list
		while(x.next != null)   //.
		{						//.
			x = x.next;			//.
		}						//.
		x.next = new Node("ned");//
		
		displayList(h);
		System.out.println();
		
		x = h; 	//sets to beginning of list
		while(x.next != null) //traverses list, stopping at end null
		{		
			if(x.next.item.equals("fred"))  //searches for item named fred
			{
				x.next = x.next.next.next;  //if fred is after node, node looks for name of object after fred
			}
			x = x.next; //sets next node to object after fred
		}
		displayList(h);
		System.out.println();	
		findName(h,"ned");
		findName(h,"jane");
		
		search = JOptionPane.showInputDialog("Enter search item: ");
		findName(h,search);
	}
	
	private static void displayList(Node h)
	{
		Node x = h;
		while(x != null)
		{
			System.out.println(x.item);
			x=x.next;
		}
	}
	
	private static void findName(Node h, String n)
	{
		int counter = 0;
		boolean found = false;
	
		System.out.println("Searching for " + n + "...");
		
		for(Node x = h; x != null; x = x.next)
		{
			if(x.item.equals(n))
			{
				found = true;
				System.out.println(n + " was located at index " + counter);
				System.out.println();
			}
			counter++;
		}
		
		if(found == false)
		{
			System.out.println(n + " has not been found within index");
		}
	}
}
