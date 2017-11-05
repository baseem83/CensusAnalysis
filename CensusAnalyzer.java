import java.io.*;

/**
    * This program processed Census information as provided through 
    * a separate file. It analyzes transactional data, summarizing 
    * it by state codes. The summarized data is output to a separate 
    * file that will subsequently be read from and reported on.
    *
    * @author Baseem Astiphan
    * @version 1.0.0.0
*/
public class CensusAnalyzer
{
    /**
        * This method is called as the startup location for the program.
        * It expects a minimum of two command line arguments, and will
        * accept a third if furnished.
        * 1. An input file from which to read census data
        * 2. An output file path, to which summarized data will be written
        * 3. If supplied, the number of records to read, otherwise all records
        *
        * precondition The input file exists and can be accessed
        * precondition The output filepath is a legal file path
        *
        * postcondition An output file will be created at designated path
        *
        * @author Baseem Astiphan
    */
    public static void main (String [] args) 
    {
        int numRecords; //Number of records to read
        String inputFile; //Input Filename
        
        //Test if at least two arguments were included, 1 input file, 1 output
        switch (args.length)
        {
            case 0: //No command line arguments
                System.out.println("\nNo input file specified. " +
                    "Exiting application......");
                return; //Exit app
            case 1: //Only 1 command line argument
                System.out.println("\nNo output file specified. " +
                    "Exiting application.....");
                return; //Exit app
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
        
        numRecords = (args.length > 2) ? 
                     Integer.parseInt(args[2]) : Integer.MAX_VALUE;
        
        //Return an array containing all of the entries from the input file up to 
        //the appropriate number of records.
        StateCensus[] stateCensus = null;
        try
        {
            //Populate stateCensus with output from readCensusData() method
            stateCensus = readCensusData(inputFile, numRecords);    
        }
        catch (Exception ex) //Catch all errors and report exception 
        {
            System.out.println("There was an error reading and analyzing the data: \n"
                                + ex.getMessage());
            return; //exit application
        }
        
        //Write the relevant data from the array into the file designated by the command
        //line argument
        try
        {
            writeDataToFile(args[1], stateCensus);
        }
        catch (Exception ex) //Catch all exceptions and print exception
        {
            System.out.println(ex);
        }
    }
    
    /**
        * This method reads in the data stored in the input file, 
        * and summarizes the data by state. It outputs the summarized
        * data to an array that holds objects of StateCensus type.
        *
        * precondition The input file exists and can be accessed
        *
        * postcondition An array exists with the relevant data
        *
        * @author Baseem Astiphan
        * @param fileName String detailing the input file's location
        * @param numRecords int limiting the number of records to read
        * @return an array of StateCensus objects
    */    
    private static StateCensus[] readCensusData(String fileName, int numRecords)
        throws FileNotFoundException, IOException, InvalidArgumentException
    {
        //Initialize an array to an appropriate length. Note: I really dislike 
        //hardcoding in a size such as I am doing here, but in Java, arrays
        //don't offer resizing flexibility, and we're not permitted to use any
        //other data structures in this assignment. I made a judgement call that
        //since there are 50 states, and a handful of territories, 60 should be
        //an appropriate length to handle all potential input files. This can 
        //be addressed with a project manager.
        StateCensus[] stateCensus = new StateCensus[60];
        
        //Number of items that have been assigned to the array; helps with tracking
        //indexes.
        int allocatedLength = 0;  
        int counter = 0; //how many records have been processed
        String str; //String to hold read in values.
        
        //Create a BufferedReader that leverages a FileReader. Since this
        //is a text file, a Reader is more appropriate than an InputStream,
        //and a BufferedReader offers some efficiency. Use try-with-resources
        //to leverage auto close
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            //Create a BufferedReader that leverages a FileReader. Since this
            //is a text file, a Reader is more appropriate than an InputStream,
            //and a BufferedReader offers some efficiency
            // br = new BufferedReader(new FileReader(fileName));
            
            //Have not reached end of file, and haven't surpassed desired records
            while ((str = br.readLine()) != null && counter < numRecords)
            {
                //Create a StateCensus object from line that is read in
                StateCensus censusItem = StateCensus.parse(str);
                
                //Nothing has been added to our output array
                if (allocatedLength == 0)
                {
                    //Add  censusItem at first position, and increment allocatedLength
                    stateCensus[allocatedLength++] = censusItem;
                    // allocatedLength++;
                }
                else
                {
                    // Use a label statement to break out of loop if a state code is found
                    found : {                     
                        //Loop through array up to allocatedLength to see if state code
                        //has already been added
                        for (int i = 0; i < allocatedLength; i++)
                        {
                            // ? stateCode exists?
                            if (censusItem.getStateCode() == stateCensus[i].getStateCode())
                            {
                                //Increment stateCensus object at found array location
                                stateCensus[i].populationIncrementer(
                                        censusItem.getTotalPopulation(),
                                        censusItem.getChildPopulation(),
                                        censusItem.getChildPovertyPopulation());
                                break found; //exit to found label
                            }
                        }
                        //add stateCensus instance to next place in array
                        stateCensus[allocatedLength++] = censusItem;
                    }
                }
                counter++;  //increment number of records read
            }
        }
        catch (FileNotFoundException ex) //File is not avaialable
        {
            throw ex; //Propagate exception to calling code
        }
        catch (IOException ex) //I/O issues
        {
            throw ex; //Propagate exception to calling code
        }
        return stateCensus; //return array of stateCensu objects
    }    
    
    /**
        * This method writes the data from a StateCensus array to a file.
        * Because the summarized data is stored in primitive types
        * (double, and int), a buffered DataOutputStream is used for writing
        * efficiently. 
        *
        * precondition The output file path is legal and accessible
        * precondition An array with StateCensus objects exists
        *
        * postcondition An output file exists.
        *
        * @author Baseem Astiphan
        * @param fileName String detailing the output file path
        * @param census an array of StateCensus objects
    */    
    private static void writeDataToFile(String fileName, StateCensus[] census)
        throws FileNotFoundException, IOException
    {
        //Use try-with-resources to create a DataOutputStream that encloses
        //a BufferedOutputStream that encloses a FileOutputStream. This design
        //decision was based on writing primitive types (ints and doubles) to 
        //a file, hence DataOutputStream. For efficiency, this stream writes to
        //a buffer that writes to the underlying file.
        try (DataOutputStream dout = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName))))
        {
            //Loop through all stateCensus objects in array
            for (StateCensus sc : census)
            {
                //This defensive pattern is necessary because we have an 
                //arbitrarily large array (since we can't resize). Once we 
                //hit a null item, this means that we have gone through all
                //stateCensus instances.
                if (sc == null)
                {
                    break;
                }
                
                //Below five lines write appropriate information to output file
                dout.writeInt(sc.getStateCode());
                dout.writeInt(sc.getTotalPopulation());
                dout.writeInt(sc.getChildPopulation());
                dout.writeInt(sc.getChildPovertyPopulation());
                dout.writeDouble(sc.getChildPovertyPercentage());
            }
        }
        catch (FileNotFoundException ex) //Input file is not available
        {
            //Propagate exception to the calling code.
            throw new FileNotFoundException(fileName + " could not be found");
        }
        catch (IOException ex)
        {
            //Propagate exception to the calling code.
            throw ex;
        }
    }
}
