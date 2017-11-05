/**
    * Extended exception used to indicate that an argument
    * to a population setter would result in an inappropriate 
    * state. For the sake of the application, inappropriate 
    * states exist if either the child population exceeds
    * the total population, or if the child poverty population
    * exceeds the child population.
    *
    * @author Baseem Astiphan
    * @version 1.0.0.0
*/
public class InvalidArgumentException extends Exception
{
    private int totalPopulation; //total population
    private int resultingChildPop; //resulting child population
    private int resultChildPovertyPop; //resulting child poverty population
    
    /**
        * Exception constructor, taking the total population,
        * the resulting child population and the resulting child 
        * poverty population.
    */
    public InvalidArgumentException(int pop, int childPop, int childPov)
    {
        //Set population values
        this.totalPopulation = pop;
        this.resultingChildPop = childPop;
        this.resultChildPovertyPop = childPov;
    }
    /**
        * Override for the getMessage method, returning the instances toString()
        *
        * @author Baseem Astiphan
        * @return String 
    */
    public String getMessage()
    {
        //return instance's toString()
        return this.toString();
    }
    
    /**
        * Override for the toString() method, creating a message
        * with the appropriate population values.
        *
        * @author Baseem Astiphan
        @ @return String
    */
    public String toString()
    {
        //Build and return output string
        return "\nInvalid argument -->\nResulting child population or " +
               "child poverty population\nwould exceed total population.\n" +
               "------------------------------------------------------------------\n" +
               "                  Total Population:  " + this.totalPopulation +
               "\n        Resultant Child Population:  " + this.resultingChildPop +
               "\nResultant Child Poverty Population:  " + this.resultChildPovertyPop;
    }
}