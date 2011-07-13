package fr.jussieu.pps.keditor.testing;

import java.util.Iterator;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.ui.IWorkbenchWindow;
import fr.jussieu.pps.kappa.core.model.KappaProgram;
import fr.jussieu.pps.keditor.ui.editor.KappaEditor;
import fr.jussieu.pps.kappa.core.scan.KappaLexer;
import fr.jussieu.pps.kappa.core.parse.KappaSymbols;

import fr.jussieu.pps.kappa.core.parse.parser;
import fr.jussieu.pps.kappa.core.parse.ast.ASTRoot;
import java_cup.runtime.Symbol;

public class parseCheck
{
	public static void main(IWorkbenchWindow window)
	{
		/** The parse tree. */
	 ASTRoot parseTree=new ASTRoot();
		
		KappaProgram program=((KappaEditor)window.getActivePage().getActiveEditor()).getProgram();
		KappaLexer lexer=new KappaLexer();
		lexer.setDocument(program.document);
		parser p=new parser();
		p.setScanner(lexer);
		try{
			System.out.println("Step1");
		Symbol result=p.parse();
		System.out.println("Step2");
		parseTree = (ASTRoot) p.ast;
		System.out.println("Parsing successful");
		if (parseTree.sig.isEmpty())
			System.out.println("it is empty");
		else
			System.out.println("it is not empty");
		if (p.parseErrors.isEmpty())
			System.out.println("it is empty");
		else
			{System.out.println("it is not empty");
			for (Iterator i = p.parseErrors.iterator(); i.hasNext(); ) {
				Symbol error = (Symbol) i.next();
				System.out.println(error.left+" "+(error.right-error.left));
			//	problems.add(new Region(error.left,error.right-error.left));
			}
			}
		}
		catch(Exception e)
		{
			System.out.println("Parsing not possible");
		}
		/*	while(i!=10)
		{
			i++;
			
			System.out.println(i+"."+s.toString()+"."+s.value.toString());
			lexer.next_token();
		}
*/		
		//System.out.println(program.document.get());
	}
}
