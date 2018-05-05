import java.util.*;
import java.io.*;

public class FriendOfAFriend2 
{

	public static void main(String[] args) throws FileNotFoundException 
	{
		String fileName = "in2.txt";
		File check = new File(fileName);
		
		if(!check.exists())
		{
			System.out.println("[-] File Does Not Exist");
			System.exit(0);
		}
		else
			runProgram(readIn(fileName));
	}
	
	private static void runProgram(ArrayList<String> rawData)
	{
		ArrayList<String> temp = new ArrayList<>();
		
		for(int i = 0; i < rawData.size() - 1; i++)
		{
			temp.add(rawData.get(i));
			if(rawData.get(i + 1).equals("")) //create pseudo-sublist and process it at breakpoints
			{
				process(temp);
				temp.clear();
				i+=2;
			}
			if(i + 1  == rawData.size() - 1) //if end of array, process the current list
			{
				temp.add(rawData.get(i + 1));
				process(temp);
			}	
		}
	}
	
	private static void process(ArrayList<String> data)
	{
		int numOfReturns = 0;
		String client = "";
		
		numOfReturns = Integer.parseInt(data.get(0));
		client = data.get(1);
		data.remove(0);
		data.remove(0);
		
		System.out.println("[*] Client: " + client);
		System.out.println("[*] Number of friends to find: " + numOfReturns);
		System.out.println("[+] Starting Search");
		display(search(friendMap(data), client, uniqueNames(data)), numOfReturns, client);
		System.out.println("\n"); //for nicer output
	}
	
	private static TreeMap<String, ArrayList<String>> friendMap(ArrayList<String> input)
	{
		TreeMap<String, ArrayList<String>> friends = new TreeMap<>();
		String name1 = "", name2 = "";
		for(int i = 0; i < input.size() - 1; i+=2)
		{
			for(int j = 0; j < 2; j++)
			{
				if(j == 0)
					name1 = input.get(i+j);
				else if(j == 1)
					name2 = input.get(i+j);
				
			}
		addTo(friends, name1, name2);
		addTo(friends, name2, name1);
		}
		
		return friends;
	}
	
	private static void addTo(Map<String, ArrayList<String>> friends, String nameA, String nameB)
	{
		if(!friends.containsKey(nameA))
			friends.put(nameA, new ArrayList<String>());
		ArrayList<String> nameAFriends = friends.get(nameA);
		nameAFriends.add(nameB);
	}
	
	private static ArrayList<String> search(TreeMap<String, ArrayList<String>> friendTree, String client, String[] people)
	{
		ArrayList<String> clientFriends = new ArrayList<>();
		ArrayList<String> friendMatch = new ArrayList<>();
		int count = 0, highestCount = 0, priority = 0;
		
		for(String person : people) //iterate through list of names and find the clients friends
			if(friendTree.get(person).contains(client))
				clientFriends.add(person);
		
		for(String person : clientFriends) 
		{
			for(String friendship : friendTree.get(person)) //searches for and ranks friends of clients friends based on common friends
			{
				if(!friendship.equals(client) && !friendTree.get(friendship).contains(client))
				{
					count = getIntersect(clientFriends, friendTree.get(friendship));
					
					if(count > highestCount)
					{
						friendMatch.add(0, friendship.toString());
						highestCount = count;
						priority++;
					}
					else if(count == highestCount)
					{
						friendMatch.add(0, friendship.toString());
					}
					else if(count == highestCount - 1)
					{
						friendMatch.add(priority, friendship.toString());
					}

					else
						friendMatch.add(friendship.toString());
					
					count = 0;
				}
			}
		}
		return friendMatch;
	}
	
    private static int getIntersect(ArrayList<String> clientsFriends, ArrayList<String> possibleMatch) //IN: hash data of both files OUT: the number of intersects
    {
    	ArrayList<String> temp = possibleMatch;
        temp.retainAll(clientsFriends);
       
        return temp.size(); //returns the amount of shared elements, as counted by the list
    }
    
    private static void display(ArrayList<String> matches, int numOfMatches, String client)
    {
    	Set<String> cleanedMatches = new LinkedHashSet<>(); //removes any possible duplicate matches
    	
    	cleanedMatches.addAll(matches);
    	matches.clear();
    	matches.addAll(cleanedMatches);
    	System.out.print("[+] " + client + " should be friends with: ");
    	for(int i = 0; i < numOfMatches; i++)
    		System.out.print(matches.get(i) + " ");
    }

	
	private static String[] uniqueNames(ArrayList<String> dataSet)
	{
		Set<String> names = new LinkedHashSet<>();
		
		names.addAll(dataSet);
		return names.toArray(new String[0]);
	}

	private static ArrayList<String> readIn(String name)
	{
		ArrayList<String> input = new ArrayList<>();
		String nonLetters = "[^a-zA-Z0-9]";
		
		try{
			BufferedReader in = new BufferedReader(new FileReader(name)); 
			String str;			
			while( (str = in.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(str);
				while(st.hasMoreTokens())
				{
					input.add(st.nextToken().replaceAll(nonLetters,"").toLowerCase());
				}
			}
			in.close();
		} catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		return input;
	}

}
