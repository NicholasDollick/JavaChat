import java.util.*;
import java.io.*;
import java.math.*;

public class Generate 
{
	//generates sequence 2^i
	public static int[] generateH1(int N) 
	{
		ArrayList a = new ArrayList<>();
		
		for(int i = 0; i < N; i++) 
	    {
	    	if((int)Math.pow(2, i) <= (.5*N)) //checks if number is less then half of the inputted data set length
	    		a.add((int)Math.pow(2, i)); //if it is, it gets added to list
	    	else
	    		break;
	    }
		
		int[] output = new int[a.size()]; //the arraylist of numbers get converted to int array to be returned
		for(int i = 0; i < output.length; i++)
		{
			output[i] = (int) a.get(i);
		}
		
		return output;
	}
	
	//generates sequence 4^i + 3 * 2^(i-1) + 1
	public static int[] generateH2(int N)
	{
		
		ArrayList a = new ArrayList<>();
		
		for(int i = 0; i < 12; i++)
		{
			int out;
			
			if(i == 0)
				out = 1;
			else
				out = (int)Math.pow(4, i) + 3 * (int)Math.pow(2, i-1) + 1;
			
			
			if(out < N * .5)
				a.add(out);
			else
				break;
			
		}
		
		int[] output = new int[a.size()];
		for(int i = 0; i < output.length; i++)
		{
			output[i] = (int) a.get(i);
		}
		
		return output;

	}
	
	//generates sequence 2^p3^q
	public static int[] generateH3(int N)
	{
		ArrayList x = new ArrayList<>();
		int p = 1;
		while(p < (N * .5))
		{
			int q = p;
			while(q < (N * .5))
			{
				x.add(q);
				q *= 3;
			}
			p *= 2;
		}
		Collections.sort(x);
	
		
		int[] output = new int[x.size()];
		for(int i = 0; i < output.length; i++)
		{
			output[i] = (int) x.get(i);
		}
		
		return output;
	}
	
	//generates sequence 2^i - 1
	public static int[] generateH4(int N)
	{
	   
		ArrayList a = new ArrayList<>();
		
		for(int i = 1; i < N; i++) 
	    {
	    	int out;
	    	
	    	out = (int)Math.pow(2, i)  - 1;
	    	
	    	if(out < N*.5)
	    		a.add(out);
	    	else
	    		break;
	    }
		
		int[] output = new int[a.size()];
		for(int i = 0; i < output.length; i++)
		{
			output[i] = (int) a.get(i);
		}
		
		return output;
	}
}