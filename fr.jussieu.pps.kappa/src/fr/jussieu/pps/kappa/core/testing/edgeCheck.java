package fr.jussieu.pps.kappa.core.testing;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;

import fr.jussieu.pps.kappa.core.parse.ast.*;
import fr.jussieu.pps.kappa.core.model.KappaProgram;

public class edgeCheck {
public static void main(IDocument d,ASTRoot a)
{
	int j=0;
	for (Iterator i = a.rul.iterator(); i.hasNext(); ) {
		Rules s=(Rules) i.next();
		try {
			String rule=d.get(d.getLineOffset(d.getLineOfOffset(s.left)),d.getLineLength(d.getLineOfOffset(s.left)));
			String reactant=rule.substring(0,rule.indexOf("->"));
			String product=rule.substring(rule.indexOf("->")+2);
		//	ruleCheck.main(d, a, s);
			
			int r=detect(reactant);
			if(r==0)
				{
			//	System.out.println("reactant is fine");
				}
			else if(r==1)
				{
				KappaProgram.problems.add(new Region(d.getLineOffset(d.getLineOfOffset(s.left)),rule.indexOf("->")));
				KappaProgram.errmsg.add("hypoegde in reactant");
			//	System.out.println("hypoegde in reactant");
				}
			else if(r==2)
			{
				KappaProgram.problems.add(new Region(d.getLineOffset(d.getLineOfOffset(s.left)),rule.indexOf("->")));
				KappaProgram.errmsg.add("hyperegde in reactant");
			//	System.out.println("hyperedge in reactant");
			}
			r=detect(product);
			if(r==0)
			{}
			//	System.out.println("product is fine");
			else if(r==1)
				{
				KappaProgram.problems.add(new Region(d.getLineOffset(d.getLineOfOffset(s.left))+rule.indexOf("->"),rule.length()-rule.indexOf("->")));
				KappaProgram.errmsg.add("hypoegde in product");	
				//System.out.println("hypoegde in product");	
				}
			else if(r==2)
			{

				KappaProgram.problems.add(new Region(d.getLineOffset(d.getLineOfOffset(s.left))+rule.indexOf("->"),rule.length()-rule.indexOf("->")));
				KappaProgram.errmsg.add("hyperegde in product");	
			}
			
		//	System.out.println(j+": "+d.get(d.getLineOffset(d.getLineOfOffset(s.left)),d.getLineLength(d.getLineOfOffset(s.left))));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		j++;
	//	break;
	}
//	System.out.println("Hey I recieved both "+j+" rules");
}

private static int detect(String reactant) {
	ArrayList<String> arr=new ArrayList<String>();
	while(reactant.indexOf('!')!=-1)
	{
		int i=reactant.indexOf('!');
		String num="";
		while(Character.isDigit(reactant.charAt(i+1)))
		{
			num=num+reactant.charAt(i+1);
			i++;
		}
		if(num!="")
		arr.add(num);
		reactant=reactant.substring(i+1);
	}
	if(arr.isEmpty())
		return 0;
	while(!arr.isEmpty())
	{
		String a=arr.get(0);
		arr.remove(0);
		if(arr.indexOf(a)==-1)
			return 1;
		else if(arr.indexOf(a)!=arr.lastIndexOf(a))
			return 2;
		arr.remove(arr.indexOf(a));
	}
	//System.out.println(arr.size());
	return 0;
}
}
