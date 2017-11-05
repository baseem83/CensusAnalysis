import java.io.*;


/**
    * This program consumes processed Census information as provided through 
    * a separate file. It reads the information from the file and prints
    * it to screen in a neatly formatted manner.
    *
    * @author Baseem Astiphan
    * @version 1.0.0.0
*/
public class CensusDataOutputReport
{
    /**
        * This method is called as the startup location for the program.
        * It expects a minimum of one command line argument, and will
        * accept a second if furnished.
        * 1. An input file from which to read census analyzed information
        * 2. If supplied, the number of records to read, otherwise all records
        *
        * precondition The input file exists and can be accessed
        *
        * postcondition Formatted Census Information printed to screen
        *
        * @author Baseem Astiphan
    */
	public static void main(String[] args)
	{
		int numRecords; //Number of records to read
        String inputFile; //Input Filename

        if (args.length == 0) //No command line arguments
        {
            System.out.println("\nNo input file specified. " +
                "Exiting application......");
                return; //Exit application
        }
        
        //Get filename of input file from command line arguments
        inputFile = args[0];
        
        //Create a temporary file to test if the file exists and is valid.
        File temp = new File(inputFile);
        
        //If file doesn't exist, or it points to a directory, quit.
        if (!temp.exists() || !temp.isFile())
        {
            System.out.println("\nThe input file does not exist. " +
                "Exiting application.....");
                return;  //Exit on error
        }        
        
        //If no command line argument is given for number of records, set this
        //value to the maximum possible value, otherwise, set numRecords to 
        //appropriate command line argument. 
        //NOTE: numRecords should never be negative, but we need not explicitly 
        //check for it to prevent errors. The method to which numRecords is 
        //passed has a loop that will never engage if the numRecords is less
        //than zero. There will just be no output.
        numRecords = (args.length > 1) ? 
                     Integer.parseInt(args[1]) : Integer.MAX_VALUE;

        //Create output report to screen
        try
        {
            generateReport(inputFile, numRecords);
        }
        catch (Exception ex) //catch all exceptions
        {
            System.out.println(ex);
            return;
        }
	}

    /**
        * This method prints the data from a StateCensus analyzed file to screen.
        * Because the summarized data is stored in primitive types
        * (double, and int), a buffered DataInputStream is used for reading
        * efficiently. 
        *
        * precondition The input file path is legal and accessible
        *
        * postcondition Analyzed information is printed to screen
        *
        * @author Baseem Astiphan
        * @param fileName String detailing the output file path
        * @param numRec total records to process
    */    
	private static void generateReport(String fileName, int numRec)
        throws FileNotFoundException, IOException
	{
		int counter = 0; //how many records have been processed
        
        //Use try-with-resources to create a DataInputStream that encloses
        //a BufferedInputStream that encloses a FileInputStream. This design
        //decision was based on reading primitive types (ints and doubles),
        //hence DataOutputStream. For efficiency, this stream reads from
        //a buffer that reads from the underlying file.        
		try (DataInputStream din = new DataInputStream(
				new BufferedInputStream(new FileInputStream(fileName)))) 
		{
            //Call helper method to generate the headings
			printHeadings(fileName);
            
            //Loop until we hit the desired number of records
			while(counter < numRec)
			{
                //Use a formatted string to output to screen the data with appropriate 
                //formatting. Will print state, totalPop, childPop, childPovPop, childPov%
				System.out.printf("   %02d  %,10d  %,16d  %,24d  %15.2f%n", 
					din.readInt(), din.readInt(), din.readInt(), din.readInt(), din.readDouble());
				
                counter++; //incremenet record counter
			}
		}
		//Cannot find the file. Should not be an issues since we defensively check for 
        //the file before entering the routine. May still arise in rare cases
        catch (FileNotFoundException ex)  
		{
            //Propagate to calling code
			throw ex;
		}
        //Reached the end of the input file. We don't want to do anything here, so no-op
		catch (EOFException ex)
		{
			//No op
		}
        //Catch any I/O exceptions
		catch (IOException ex)
		{
            //Propagate to calling code
			throw ex;
		}
		catch (Exception ex) //Catch all other exceptions
		{
            //Propagate to calling code
			throw ex;
		}
	}

    /**
        * Helper method to encapsulate logic for printing headers to the screen
        *
        * @author Baseem Astiphan
        * @param fileName String file name to be printed as part of the header
    */
	private static void printHeadings(String fileName)
	{
        //Print full file path to the screen
		System.out.println("\nFile: " + new File(fileName).getAbsolutePath());

        //Print column headings
		System.out.print("\nState  ");
		System.out.print("Population  ");
		System.out.print("Child Population  ");
		System.out.print("Child Poverty Population  ");
		System.out.print("% Child Poverty\n");

        //Print borders, for formatting purposes
		System.out.print("-----  ");
		System.out.print("----------  ");
		System.out.print("----------------  ");
		System.out.print("------------------------  ");
		System.out.print("---------------\n");


	}

}