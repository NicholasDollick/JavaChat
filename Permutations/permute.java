import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class dictionary
{
	public static ArrayList<String> getDict()
	{
		ArrayList<String> dict = new ArrayList<>();
		
		try{
			Scanner input = new Scanner(new File ("dictionary.txt"));
			while(input.hasNext())
				dict.add(input.next());
			
			input.close();
		}catch(FileNotFoundException e){
			System.out.println("File Not Found");
		}
		
		return dict;
	}
}

public class permute 
{
	public static void main(String [] args) throws FileNotFoundException
	{	
		String word = "swap";
		int n = word.length();
		
		permute(word, 0, n-1);
	}
	
	public static void permute(String str, int s, int r)
	{
		if(r==s)
			compare(str);
		else
		{
			for(int i = s; i <= r; i++)
			{
				str = swap(str, s, i);
				permute(str, s+1, r);
				str = swap(str, s, i);
			}
		}
	}
	
	public static String swap(String a, int i, int j)
	{
		char temp;
		char[] charArr = a.toCharArray();
		temp = charArr[i];
		charArr[i] = charArr[j];
		charArr[j] = temp;
		return String.valueOf(charArr);
	}
	
	public static void compare(String word)
	{
		ArrayList<String> dict = dictionary.getDict();
		for(String a : dict)
			if(a.equals(word))
				System.out.println(word);
	}
}
