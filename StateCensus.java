/**
    * This class represents a single state from the Census calculations.
    * It helps to maintain meta information about the state, such as 
    * the state code in the reporting system, as well as state findings.
    *
    * @author Baseem Astiphan
    * @version 1.0.0.0
*/
public class StateCensus
{
    // Member variable to hold the state code. Considered otherwise
    // holding this information as a String, but decided to use int
    // and leverage formatting options in printf
    private int stateCode; 
    private int totalPopulation;  //Total population for state
    private int childPopulation;  //Total child population for state
    private int childPovertyPopulation; //Child Poverty population for state
    
    /**
        * Convenience constructor, taking as a parameter a census line
        * from the Census file. This constructor calls the static 
        * StateCensus.parse() method to return a StateCensus object,
        * then adds the appropriate attributes to 'this'.
    */
    public StateCensus(String line) throws InvalidArgumentException
    {
        //Return a StateCensus object with appropriate values set
        StateCensus ci = StateCensus.parse(line);
        
        //Below 4 lines set 'this' attributes to those of the newly created
        //StateCensus instance.
        this.setStateCode(ci.getStateCode());
        this.setTotalPopulation(ci.getTotalPopulation());
        this.setChildPopulation(ci.getChildPopulation());
        this.setChildPovertyPopulation(ci.getChildPovertyPopulation());
    }
    
    /**
        * No-op default constructor created for convenience. This must be 
        * created explicitly since there is another constructor.
    */
    public StateCensus()
    {}
    
    /**
        * Method to return the stateCode.
        *
        * @author Baseem Astiphan
        * @return stateCode integer
    */
    public int getStateCode()
    {
        return stateCode;  //return the state code
    }
    
    /**
        * Method to set the state code. Since there are no known constraints
        * on state codes, this takes any integer. Perhaps future conversations
        * with project manager or sponsor could indicate whether this should 
        * be limited to certain values.
        *
        * @author Baseem Astiphan
        * @param stateCode integer for the state code
    */
    public void setStateCode(int stateCode)
    {
        //Set stateCode to the argument
        this.stateCode = stateCode;
    }
    
    /**
        * Method to return the totalPopulation.
        *
        * @author Baseem Astiphan
        * @return totalPopulation integer
    */    
    public int getTotalPopulation()
    {
        return totalPopulation; //return total population
    }

    /**
        * Method to set the total population. Reasonably, we should consider
        * constraining this since total population is not infinite, but 
        * the conversation should be had with a project manager.
        *
        * @author Baseem Astiphan
        * @param totalPop integer for the total population
    */    
    public void setTotalPopulation(int totalPop)
    {
        //Set totalPopulation to the argument
        totalPopulation = totalPop;
    }
    
    /**
        * Method to return the childPopulation.
        *
        * @author Baseem Astiphan
        * @return childPopulation integer
    */       
    public int getChildPopulation()
    {
        return childPopulation; //return childPopulation
    }
    
    /**
        * Method to set the child population. Naturally, the child population
        * must always be less than or equal to total population 
        * (realistically, should be less than, unless children run the town 
        * and have kicked out all adults).
        *
        * Do note, this may cause issues if a user tries to set the child 
        * population before the totalPop is set. Discuss with project
        * manager to confirm this 'feature' should be included in class 
        * defintion.
        *
        * @author Baseem Astiphan
        * @param childPop integer for the child population
    */      
    public void setChildPopulation(int childPop) throws InvalidArgumentException
    {
        //Throw exception if the resulting population would exceed total pop
        if (childPop > this.getTotalPopulation())
        {
            throw new InvalidArgumentException(
                this.getTotalPopulation(), 
                childPop,
                this.getChildPovertyPopulation());
        }
        childPopulation = childPop; //If no exception, child pop is set
    }
    
    /**
        * Method to return the childPovertyPopulation.
        *
        * @author Baseem Astiphan
        * @return childPovertyPopulation integer
    */    
    public int getChildPovertyPopulation()
    {
        //return child poverty pop
        return childPovertyPopulation;
    }
    
    /**
        * Method to set the child poverty population. Naturally, the child 
        * poverty pop must always be less than or equal to the child population 
        *
        * Do note, this may cause issues if a user tries to set the child 
        * poverty population before the childPopulation is set. Discuss with project
        * manager to confirm this 'feature' should be included in class 
        * defintion.
        *
        * @author Baseem Astiphan
        * @param childPoverty integer for the total poverty pop
    */        
    public void setChildPovertyPopulation(int childPoverty) throws InvalidArgumentException
    {
        //Throw exception if the resulting child poverty pop exceeds child pop
        if (childPoverty > this.getChildPopulation())
        {
            throw new InvalidArgumentException(
                this.getTotalPopulation(), 
                this.getChildPopulation(),
                childPoverty);            
        }
        childPovertyPopulation = childPoverty; //If no exception, poverty is set
    }
    
    /**
        * Convenience method to increment this population values. This is especially
        * useful in analyzing data, as information is read from a file one item
        * at a time, and should be added to an aggregate total.
        *
        * @author Baseem Astiphan
        * @param totalPop integer for total population incrementation
        * @param childPop integer for the child population incrementation
        * @param childPovPop integer for the child poverty population incrementation
    */          
    public void populationIncrementer(int totalPop, int childPop, int childPovPop)
        throws InvalidArgumentException
    {
        //Below three lines add the current population value with the incremented value,
        //then set the new values, while adhering to constraints
        setTotalPopulation(totalPopulation + totalPop);
        setChildPopulation(childPopulation + childPop);
        setChildPovertyPopulation(childPovertyPopulation + childPovPop);
    }
  
    
    /**
        * Calculated method to return the percentage of children living in 
        * poverty. It is calculated as the childPovertyPopulation divided by
        * the total child population. This number is then multiplied by 100
        * to return as a percentage. (Note, both values cast to double for
        * precision);
        *
        * @author Baseem Astiphan
        * @return childPovertyPercentage double
    */   
    public double getChildPovertyPercentage()
    {
        //Calculate and return the childPovertyPercentage
        return 100 * (double)getChildPovertyPopulation() /
               (double)getChildPopulation();
    }
    
    /**
        * Convenience STATIC method to return a StateCensus instance object
        * from a String representation. The String makeup is defined in the layout
        * file as provided by the Census bureau. This method allows for easy
        * transition from text line into an object.
        *
        * It is important to note that this method relies on the layout of
        * a Census line item remaining exactly the same. Changes to lines item
        * layout will interfere with this method. Unfortunately, there are 
        * no delimters in the file that could be leveraged.
        *
        * precondition line parameter must meet Census bureau layout
        *
        * @author Baseem Astiphan
        * @param line String as formatted according to Census Bureau
    */    
    public static StateCensus parse(String line) throws InvalidArgumentException
    {
        //Create new instance
        StateCensus ci = new StateCensus();
 
        //Below 4 lines parse substrings from the line to integers, then set
        //the appropriate population variables
        ci.setStateCode(Integer.parseInt(line.substring(0,2).trim()));       
        ci.setTotalPopulation(Integer.parseInt(line.substring(82, 90).trim()));
        ci.setChildPopulation(Integer.parseInt(line.substring(91, 99).trim()));
        ci.setChildPovertyPopulation(Integer.parseInt(line.substring(100, 108).trim()));
        
        return ci; //return newly created object
    }
    
    /**
        * Override method for toString(). Returns state code and population values
        * formatted as Strings.
        *
        * @author Baseem Astiphan
        * @return information about object String 
    */    
    public String toString()
    {
        return String.valueOf(getStateCode()) + " " +
                    String.valueOf(getTotalPopulation()) + " " +
                    String.valueOf(getChildPopulation()) + " " +
                    String.valueOf(getChildPovertyPopulation()) + " " +
                    String.valueOf(getChildPovertyPercentage());
    }
    
    /**
        * Below main method exists to support the unit testing below. 
        * It need not be called from a user application.
    */
    public static void main(String [] args)
    {
        UnitTests.createStateCensusDefaultCtor();
        UnitTests.createStateCensusParsedString();
        UnitTests.childPovertyPercentageCalculatesCorrectly();
        UnitTests.populationIncrementersWorkCorrectly();
        UnitTests.childPopulationCannotExceedTotalPopulation();
        UnitTests.childPovertyPopulationCannotExceedChildPopulation();
    }
}

/**
    * Below class is self documenting and contains unit tests. These tests are 
    * run in the main method and should be called whenever changes are made to 
    * ensure that nothing else has broken. (They have come in quite handy). 
    *
    * As applications become more complex and more difficult to test (it's not
    * as easy to test reading from a file as it is keyboard input), automated 
    * methods that test for us become much more important.
*/
class UnitTests
{
    static void createStateCensusDefaultCtor()
    {
        StateCensus sc = new StateCensus();
        assert (sc instanceof StateCensus) : "Failed to create an instance";
    }
    
    static void createStateCensusParsedString()
    {
        try
        {
            StateCensus sc = StateCensus.parse
                   ("01 00190 Alabaster City School District                                              " +
                        "31754     6475      733 USSD13.txt 24NOV2014  ");
            
            assert (sc instanceof StateCensus) : "Failed to create an instance";
            assert (sc.getStateCode() == 1) : "Incorrect state code";
            assert (sc.getTotalPopulation() == 31754) : "Incorrect total population";
            assert (sc.getChildPopulation() == 6475) : "Incorrect child population";
            assert (sc.getChildPovertyPopulation() == 733) : "Incorrect child poverty population";            
        }
        catch (Exception ex) 
        {
            System.out.println("createStateCensusParsedString Failed");
        }

    }
    
    static void childPovertyPercentageCalculatesCorrectly()
    {
        try
        {
            StateCensus sc = StateCensus.parse
                   ("01 00190 Alabaster City School District                                              " +
                        "31754     6475      733 USSD13.txt 24NOV2014  ");
            assert (sc.getChildPovertyPercentage() == (100 * (733.0 / 6475.0))) : "Incorrect Child Poverty %";            
        }
        catch (Exception ex)
        {
            System.out.println("childPovertyPercentageCalculatesCorrectly Failed");
        }

    }
    
    static void populationIncrementersWorkCorrectly()
    {
        try
        {
            StateCensus sc = StateCensus.parse
                   ("01 00190 Alabaster City School District                                              " +
                        "31754     6475      733 USSD13.txt 24NOV2014  ");
            
            sc.populationIncrementer(1, 2, 3);
            
            assert (sc.getTotalPopulation() == 31755) : "Incorrect total population";
            assert (sc.getChildPopulation() == 6477) : "Incorrect child population";
            assert (sc.getChildPovertyPopulation() == 736) : "Incorrect child poverty population";        
        }
        catch (Exception ex)
        {
            System.out.println("populationIncrementersWorkCorrectly Failed");
        }
    }
    
    static void childPopulationCannotExceedTotalPopulation()
    {
        try
        {
            StateCensus sc = StateCensus.parse
                   ("01 00190 Alabaster City School District                                              " +
                        "31754     6475      733 USSD13.txt 24NOV2014  ");
            sc.populationIncrementer(0, 100, 0);
        }
        catch(InvalidArgumentException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    static void childPovertyPopulationCannotExceedChildPopulation()
    {
        try
        {
            StateCensus sc = StateCensus.parse
                   ("01 00190 Alabaster City School District                                              " +
                        "31754     6475      733 USSD13.txt 24NOV2014  ");
            sc.populationIncrementer(0, 0, 100);
        }
        catch(InvalidArgumentException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
