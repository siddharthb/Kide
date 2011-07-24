package fr.jussieu.pps.kappa.core.model;

import fr.jussieu.pps.kappa.core.parse.parser;
import fr.jussieu.pps.kappa.core.parse.ast.ASTRoot;
import fr.jussieu.pps.kappa.core.scan.KappaLexer;
import fr.jussieu.pps.kappa.core.testing.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java_cup.runtime.Symbol;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * Maintains the high-level representation of an SML program,
 * including the parse tree and the identifiers defined in the program.
 * <p>
 * An SmlProgram works on one document being edited in the workspace.
 * It has to be updated by the SmlEditor whenever the document is changed.
 */
public class KappaProgram {
	
	/** The document containing the program source. */
	public IDocument document;
	/** The list of listeners registered to be notified of updates. */
	private List listeners;
	
	//This problem that is fixed can help building a incremental parser
	/** Every SmlProgram keeps its own lexer and parser. */
//	private KappaLexer lexer;
	/** Every SmlProgram keeps its own lexer and parser. */
//	private parser parser;
	
	/** The parse tree. */
	private ASTRoot parseTree;
	/** The list of bindings. */
	private List bindings;
	/** The list of problems. */
	public static List problems;
	public static List errmsg;

	/** Creates a SmlProgram attached to the given document.
	 * The SmlProgram does not update itself when the document changes. */
	public KappaProgram(IDocument document) {
		this.document = document;
		listeners = new ArrayList();
	}
	
	/** Returns the parse tree of the program. */
	public ASTRoot getParseTree () {
		if (parseTree == null)
			update();
		return parseTree;
	}
	
	/** Returns the list of identifiers bound by the program.
	 * See SmlBinding for details. */
	public KappaBinding[] getBindings () {
		if (bindings == null)
			update();
		return (KappaBinding[]) bindings.toArray(new KappaBinding[0]);
	}
	
	/** Returns the regions where problems were found in the program.
	 * In this version, only syntax errors are found. */
	public IRegion[] getProblems () {
		if (problems == null)
			update();
		return (IRegion[]) problems.toArray(new IRegion[0]);
	}
	public String[] getErrMsg () {
		if (errmsg == null)
			update();
		return (String[]) errmsg.toArray(new String[0]);
	}
	
	
	/** Updates the representation of the program. This method should
	 * be called by the editor when the document is changed. */
	public void update () {
		parse();
		bindings = KappaBinding.getBindings(parseTree);
	//	System.out.println("bindings: "+Integer.toString(bindings.size()));
	//	System.out.println("signatures: "+Integer.toString(parseTree.sig.size()));
		fireModelChanged();
	}
	
	/** Updates the parse tree and the list of problems. */
	private void parse () {
		KappaLexer lexer=new KappaLexer();
		parser parser=new parser();
		if (lexer == null)
			lexer = new KappaLexer();
		lexer.setDocument(document);
		if (parser == null)
			parser = new parser();
		parser.setScanner(lexer);
		
		problems = new ArrayList();
		errmsg = new ArrayList();
		try {
			Symbol result = parser.parse();
			for (Iterator i = parser.parseErrors.iterator(); i.hasNext(); ) {
				Symbol error = (Symbol) i.next();
				problems.add(new Region(error.left,error.right-error.left));
				errmsg.add("Syntax error");
			}
			parseTree = (ASTRoot) parser.ast;
			edgeCheck.main(document,parseTree);
			
		} catch (Exception e) {
			parseTree = new ASTRoot();
		}
	}

	/** Adds a listener to this program. The listener will be notified
	 * when the program is updated. */
	public void addListener (IKappaProgramListener listener) {listeners.add(listener);}
	/** Removes the listener from this program. If the listener was not
	 * added to the program, nothing happens. */
	public void removeListener (IKappaProgramListener listener) {listeners.remove(listener);}
	/** Notifies all listeners that the program has been updated. */
	private void fireModelChanged () {
		for (Iterator i = listeners.iterator(); i.hasNext(); )
			((IKappaProgramListener) i.next()).programChanged(this);
	}

	/** Returns the deepest identifier bound at the given offset
	 * in the document. If there is no binding at the offset,
	 * null is returned. */
	public KappaBinding getBinding (int offset) {
		return getDeepestBinding(offset, getBindings());
	}
	
	/** Recursively searches among the given bindings and their children
	 * to find the deepest binding containing the given offset. */
	private KappaBinding getDeepestBinding (int offset, KappaBinding[] bindings) {
		for (int i = 0; i < bindings.length; i++) {
			KappaBinding binding = bindings[i];
			if (containsOffset(binding,offset)) {
				return binding;
			}
		}
		return null;
	}

	private boolean containsOffset (KappaBinding binding, int offset) {
		return offset >= binding.getLeft() && offset <= binding.getRight(); 
	}

}