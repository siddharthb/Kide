package fr.jussieu.pps.kappa.core.testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;

import fr.jussieu.pps.kappa.core.model.KappaBinding;
import fr.jussieu.pps.kappa.core.model.KappaProgram;
import fr.jussieu.pps.kappa.core.parse.ast.ASTRoot;
import fr.jussieu.pps.kappa.core.parse.ast.VAR_ALG;
import fr.jussieu.pps.kappa.core.parse.ast.VAR_KAPPA;
import fr.jussieu.pps.kappa.core.parse.ast.Variables;

public class duplicateCheck {
	public static void main(IDocument d,ASTRoot a)
	{
		List<String> v=new ArrayList<String>();
		for (Iterator i = a.var.iterator(); i.hasNext(); ) {
			Variables s=(Variables) i.next();
			v.add(s.name);
		}
		for (Iterator i = a.var.iterator(); i.hasNext(); ) {
			Variables s=(Variables) i.next();
			if(v.indexOf(s.name)==v.lastIndexOf(s.name))
			{
				
			}
			else
			{
				KappaProgram.problems.add(new Region(s.left,s.right-s.left));
				KappaProgram.errmsg.add("Duplicate name");
			}
		}
	//	System.out.println(v.size());
	}
}
