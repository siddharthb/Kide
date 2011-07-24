
package fr.jussieu.pps.keditor.wizards;

/**
 * Model class containing the data for the holiday wizard
 */
public class SimulateModel 
{

	// holiday destination
	protected String events="";
	
	// holiday departure point
	protected String time="";
	
	protected String commandLine="";
	// flag indicating whether the user buys insurance when traveling by car
	protected boolean buyInsurance;


	public String toString()
	{
		String s;
		s= "Your holiday: \n";
		 s = s+"Driving from ";
		s= s + time + " to "+  events;
			if (buyInsurance) s = s+ "\nbuy insurance from the rental company";
			else  s = s +"\ndo not buy insurance from the rental company";
		return s;	
	}
}
