package fr.jussieu.pps.kappa.core.scan;

import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 * This class performs the basic lexical analysis (scanning) of a document.
 * It expects to be run only on "plain" SML code without comments or strings,
 * since those should have been divided into separate partitions by an
 * SmlPartitionScanner. SmlLexer uses it in this manner to read
 * tokens from SML code.
 * <p>
 * The token scanner does not check for SML keywords. It returns all potential
 * identifiers and keywords as SmlTokenTypes.WORD.
 */
public class KappaTokenScanner extends BufferedRuleBasedScanner {

	public KappaTokenScanner () {
		super();
		
		IToken wordToken = new Token(KappaTokenTypes.WORD);
	//	IToken labelToken = new Token(KappaTokenTypes.LABEL);
	//	IToken tyvarToken = new Token(KappaTokenTypes.TYVAR);
		IToken intToken = new Token(KappaTokenTypes.INT);
		IToken realToken = new Token(KappaTokenTypes.REAL);
		
		IRule whitespace = new WhitespaceRule(new WhitespaceDetector());
		IRule numeric = new KappaNumericRule(intToken, realToken);
	//	IRule label = new KappaLabelRule(labelToken);
		
		
		WordRule IDWords = new WordRule(new IDWordDetector(), wordToken);
		//WordRule ID1Words = new WordRule(new ID1WordDetector(), wordToken);
		IRule ID1Words = new KappaInstrRule(wordToken);
		WordRule pertWords = new WordRule(new pertWordDetector(), wordToken);
		WordRule symWords = new WordRule(new symWordDetector(), wordToken);
		
		WordRule internalStateWords = new WordRule(new internalStateDetector(), wordToken);
	//	WordRule primedWords = new WordRule(new PrimedWordDetector(), tyvarToken);
		IRule radius = new KappaRadiusRule(wordToken);
		
		IRule punctuation = new KappaPunctuationRule(wordToken);
		
		setRules(new IRule[] {whitespace, numeric, IDWords,ID1Words,pertWords,symWords, internalStateWords,radius, punctuation});
	}
	
	private static class WhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace (char c) {return (c==' ' || c=='\t' || c=='\f');}
	}
	private static class IDWordDetector implements IWordDetector {
		public boolean isWordStart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z')|| (c>='0' && c<='9'));}
		public boolean isWordPart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='_' || c=='-');}
	}
	/*private static class ID1WordDetector implements IWordDetector {
		public boolean isWordStart (char c) {return c=='%';}
		public boolean isWordPart (char c) {return ((c>='a' && c<='z') || c==':');}
	}*/
	private static class pertWordDetector implements IWordDetector {
		public boolean isWordStart (char c) {return c=='$';}
		public boolean isWordPart (char c) {return ((c>='A' && c<='Z'));}
	}
	private static class symWordDetector implements IWordDetector {
		public boolean isWordStart (char c) {return c=='[';}
		public boolean isWordPart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z') || c==']');}
	}
	private static class internalStateDetector implements IWordDetector {

		public boolean isWordStart (char c) {return c=='~';}
		public boolean isWordPart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') );}
	}
	/*
	private static class PrimedWordDetector implements IWordDetector {
		public boolean isWordStart (char c) {return c=='\'';}
		public boolean isWordPart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='_' || c=='\'');}
	}*/
	
}