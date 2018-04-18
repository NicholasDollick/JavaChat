import java.io.*;
import java.util.*;

public class Similarity {

	public static void main(String[] args)
	{
		String[] fileNames = {args[0], args[1]}; //list of file names to be compared, can also be read from file
		ArrayList<ArrayList<Long>> toCompare = new ArrayList<>(); //create list of data to be compared
		int n = Integer.parseInt(args[2]); //the size of the n-gram "shingles"
		
		System.out.println("[*] Starting Comparison for: " + fileNames[0] + " and " + fileNames[1]); 
		for(String name : fileNames) //loop through file names and process
			toCompare.add(go(name, n)); //processes the data and stores it in array for later use
		
		comparison(toCompare); //compares the data and displays the results
	}

	private static ArrayList<Long> go(String fileName, int n) //IN: File name and size of n-gram. OUT: List of hash data
	{
		ArrayList<String> inputs = new ArrayList<String>();
		ArrayList<Long> hashed = new ArrayList<Long>();
		
		File check = new File(fileName);
		if(!check.exists())
		{
			System.out.println("[-] File Does Not Exist");
			System.exit(0);
		}
		else
			inputs = readIn(fileName); //if file is good, call the function(40) to read the data from the file
		
		hashed = shingle(inputs, n); //send read data to function(64) to shingle into n-grams
		
		return hashed; //return the hash list
	}
	
	private static ArrayList<String> readIn(String name) //IN: a filename OUT: List of semi-formatted file contents
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
	
	private static ArrayList<Long> shingle(ArrayList<String> words, int n) //IN: raw data and n-gram size
	{
		ArrayList<String> count = new ArrayList<>(); 
		ArrayList<Long> hash = new ArrayList<>();
		String sb = "";
		
		for(int i = 0; i < words.size() - n + 1; i++)
		{
			for(int j = 0; j < n; j++)
				sb = sb + words.get(i + j) + " ";
			
			if(!count.contains(sb))
			{
				count.add(sb);
				hash.add(getHash(sb));
			}
			sb = "";
		}
		
		return hash;
	}
	
	private static long getHash(String x) //IN: string, in this case word or n-gram OUT: hash value
	{
		long M = ((long)Math.pow(2,32)) - 1;
		long hash = 0;
		for(int i = 0; i < x.length(); i++)
		{
			hash = (31* hash + x.charAt(i)) % M;
		}
		
		return hash;
	}
	
	
	private static int getIntersect(ArrayList<Long> x, ArrayList<Long> y) //IN: hash data of both files OUT: the number of intersects
	{
		ArrayList<Long> temp = x;
		temp.retainAll(y);
		
		return temp.size(); //returns the amount of shared elements, as counted by the list
	}
	
	
	
	private static int checkUnion (ArrayList<Long> x, ArrayList<Long> y) //IN: has data of both files OUT: the total unique elements
	{
		Set<Long> union = new HashSet<>(); 
		
		union.addAll(x);
		union.addAll(y);

		return union.size(); //return the total number of unique words contained in both sets
	}
	
	
	private static void comparison(ArrayList<ArrayList<Long>> test) //IN:List of all relevant data OUT: formatted findings
	{
		int unionSize = 0, intersectSize = 0;
		
		unionSize = checkUnion(test.get(0), test.get(1));
		intersectSize = getIntersect(test.get(0), test.get(1));
		
		System.out.println("[+] The two bodies of work are: " + intersectSize + "/" + unionSize + " similar");
		System.out.println("[+] The two bodies of work are: " + Math.round(((double)intersectSize / unionSize)*100) + "% similar");
	}


}
