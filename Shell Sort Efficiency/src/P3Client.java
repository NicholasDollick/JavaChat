import java.io.*;
import java.util.*;
import java.math.*;

//Nicholas Dollick


public class P3Client 
{

	public static void main(String [] args)
	{
		
		double duration1 = 0,duration2 = 0,duration3 = 0,duration4 = 0;
		int h1count = 0, h2count = 0, h3count = 0, h4count = 0;
		int[] h1, h2, h3, h4;
		int numberOfTrials = Integer.parseInt(args[1]);
		List<Double> input = new ArrayList<>();
		String file = args[0];
		
		//reads in contents of file
		try{
			BufferedReader in = 
					new BufferedReader( new FileReader(file));
			String str;
			
			while( (str = in.readLine()) != null)
			{
				input.add(Double.parseDouble(str)); //data gets stored into an arraylist
			}
				
			in.close();
			
		} catch(IOException e) {
		System.out.println(e.getMessage());
	}
	
		
		
		
		
		int N = input.size(); //saves total number of elements inside data set
		Double[] data =  input.toArray(new Double[0]); //converts arraylist into an array of Doubles
		
		//generates the 4 sequences based on number of elements
		h1 = Generate.generateH1(N);
		h2 = Generate.generateH2(N);
		h3 = Generate.generateH3(N);
		h4 = Generate.generateH4(N);
		
		
		
		
		//calls the method that runs the timed tests on each sequence 
		duration1 = timedTest(data, h1, numberOfTrials)/1000000;
		h1count = ShellSort.getCount();
		duration2 = timedTest(data, h2, numberOfTrials)/1000000;
		h2count = ShellSort.getCount();
		duration3 = timedTest(data, h3, numberOfTrials)/1000000;
		h3count = ShellSort.getCount();
		duration4 = timedTest(data, h4, numberOfTrials)/1000000;
		h4count = ShellSort.getCount();
		


		//begin output block
		System.out.println("------------------------------------------------------------------------------------------------------------");
		System.out.println("The number of trials is: " + numberOfTrials);
		System.out.println();
		System.out.println("seq 1:"); 			//sequences printed in descending order
		for(int i = h1.length-1; i >= 0; i--)
		{
			System.out.print(h1[i] + " ");
		}
		System.out.println();
		System.out.println("seq 2:");
		for(int i = h2.length-1; i >= 0; i--)
		{
			System.out.print(h2[i] + " ");
		}
		System.out.println();
		System.out.println("seq 3:");
		for(int i = h3.length-1; i >= 0; i--)
		{
			System.out.print(h3[i] + " ");
		}
		System.out.println();
		System.out.println("seq 4:");
		for(int i = h4.length-1; i >= 0; i--)
		{
			System.out.print(h4[i] + " ");
		}
		System.out.println();
		System.out.println();
	    System.out.println("Comparisons:");
	    System.out.printf("\nAverage comparisons for Sequence 1 is %d\n",h1count);
	    System.out.printf("Average comparisons for Sequence 2 is %d\n",h2count);
	    System.out.printf("Average comparisons for Sequence 3 is %d\n",h3count);
	    System.out.printf("Average comparisons for Sequence 4 is %d\n",h4count);
		
	    System.out.println("----------------------------------------");
	    System.out.println("Average Time:");
	    System.out.println();
		System.out.printf("Average Time using Sequence h[i] = 2^i for %d trials is             %.6f ms\n",numberOfTrials,duration1);
		System.out.printf("Average Time using Sequence h[i] = 4^i+3*2^i + 1 for %d trials is   %.6f ms\n",numberOfTrials,duration2);
		System.out.printf("Average Time using Sequence h[i] = 2^p*3^q for %d trials is         %.6f ms\n",numberOfTrials,duration3);
		System.out.printf("Average Time using Sequence h[i] = 2^i - 1 for %d trials is         %.6f ms\n",numberOfTrials,duration4);
		System.out.println("----------------------------------------");
		System.out.println("Ratios:");
		System.out.println();
		System.out.printf("T1/T2 = %.3f ms\n",(duration1/duration2));
		System.out.printf("T1/T3 = %.3f ms\n",duration1/duration3);
		System.out.printf("T1/T4 = %.3f ms\n",duration1/duration4);
		System.out.printf("T2/T3 = %.3f ms\n",duration2/duration3);
		System.out.printf("T2/T4 = %.3f ms\n",duration2/duration4);
		System.out.printf("T3/T4 = %.3f ms\n",duration3/duration4);
		System.out.println("------------------------------------------------------------------------------------------------------------");
		//end output block
	}
		
	public static double timedTest(Double[] data, int[] sequence, int numTrials)
		{
			double average = 0.0; //measures average time
			for(int i = 0; i < numTrials; i++)
			{
				ShellSort study = new ShellSort(data); //creates new instance of ShellSort
				long start = System.nanoTime();
				study.sortUsing(sequence); //sorts the data set using the corresponding sequence
				long duration = (System.nanoTime() - start);
				average += duration;
			}
			average /= numTrials;
			
			return average; //returns average time
		}
	
}
