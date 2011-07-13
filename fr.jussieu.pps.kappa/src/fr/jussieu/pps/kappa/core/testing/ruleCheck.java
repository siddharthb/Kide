package fr.jussieu.pps.kappa.core.testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;

import fr.jussieu.pps.kappa.core.model.KappaProgram;
import fr.jussieu.pps.kappa.core.parse.ast.*;

public class ruleCheck {

	public static void main(IDocument d,ASTRoot a,Rules r)
	{
		 List<String> arr;
		 List<List<String>> port;
		 List<List<List<String>>> state;
		arr = new ArrayList<String>();
		port = new ArrayList<List<String>>();
		state = new ArrayList<List<List<String>>>();
		
		for (Iterator i = a.sig.iterator(); i.hasNext(); ) 
		{
			Signatures s=(Signatures) i.next();
			arr.add(s.a.ag_nme);
			
			List<String> arr1;
			arr1 = new ArrayList<String>();
			
			boolean j=true;
			agent b=s.a;
			while(j)
			{
				if(b.ag_intf instanceof PORT_SEP)
				{
				//	System.out.println("hey");
					PORT_SEP p=(PORT_SEP)b.ag_intf;
					arr1.add(p.v1.port_nme);
					b.ag_intf=p.v3;
					
					port.add(p.v1.port_int.v1);
				
					
				}
				else
					j=false;
			}
			port.add(arr1);
		}
		System.out.println(port.get(2).size());
		
/*		if (r.v2.v3 instanceof COMMA)
		{
		COMMA c=(COMMA)r.v2.v3;
		for (Iterator i = a.sig.iterator(); i.hasNext(); ) {
			Signatures s=(Signatures) i.next();
			if(agentComp(c.v1,s.a))
			{

				System.out.println("3 is called");
				main(d,a,new Rules(r.v1,new rule(c.v2,r.v2.v5,r.v2.v4,r.v2.v1)));
				return 0;
			}
		}
		System.out.println("1 is called");
		KappaProgram.problems.add(new Region(c.v1.left,c.v1.right-c.v1.left));
		KappaProgram.errmsg.add("Undefined site in agent");	
		}
		else if (r.v2.v3 instanceof EMPTY_MIX)
		{
			
			{
				return 0;
			}
		}
	//	System.out.println("EMPTY_MIX");
		return 0;
*/		
	}

	private static List<String> getSites(agent a)
	{
		List<String> arr=new ArrayList<String>();
		boolean i=true;
		agent b=a;
		while(i)
		{
			if(b.ag_intf instanceof PORT_SEP)
			{
			//	System.out.println("hey");
				PORT_SEP p=(PORT_SEP)b.ag_intf;
				arr.add(p.v1.port_nme);
				b.ag_intf=p.v3;
			}
			else
				i=false;
		}
		return arr;
	}
	private static boolean agentComp(agent v1, agent a) {
		
		if(v1.ag_nme.compareTo((a.ag_nme))==0)
		{
			List<String> arr1=getSites(v1);
			System.out.println(arr1.size());
			List<String> arr2=getSites(a);
			System.out.println(arr2.size());
			while(!arr1.isEmpty())
			{
				if(arr2.isEmpty())
					return false;
				if(arr2.contains(arr1.get(0)))
				{
					arr1.remove(0);
					continue;
				}
				return false;
			}
			return true;
		}
		else
		{
		System.out.println(v1.ag_nme+" "+a.ag_nme);
			return false;
		}
		
		}

	private static boolean interfaceComp(interface1 agIntf, interface1 agIntf2) {
		if(agIntf instanceof PORT_SEP)
		{
			if(agIntf2 instanceof PORT_SEP)
			{
				PORT_SEP p1=(PORT_SEP)agIntf;
				PORT_SEP p2=(PORT_SEP)agIntf2;
				if(p1.v1.port_nme==p2.v1.port_nme)
				{}
			}
			else if(agIntf2 instanceof EMPTY_INTF)
			{
				return false;
			}
		}
		else if(agIntf instanceof EMPTY_INTF)
			return true;
		return false;
	}
}
