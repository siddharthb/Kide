package fr.jussieu.pps.kappa.core.scan;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/** A rule that matches SML punctuation which does not fall into any other rule.
 * Used by SmlTokenScanner. */
public class KappaRadiusRule implements IRule {
	private IToken token;
	public KappaRadiusRule (IToken successToken) {token = successToken;}
	public IToken evaluate (ICharacterScanner scanner) {
		int c= scanner.read();
		if (c == '.'||c=='+')	
		{
			if(scanner.read()=='{')
			{
				int length = readDigits(scanner);
				if(length==0)
				{
					scanner.unread();
					scanner.unread();
					return Token.UNDEFINED;
				}
				else
				{
					if(scanner.read()=='}')
						return token;
					else
					{
						for(int i=1;i<=length+2;i++)
							scanner.unread();
						return Token.UNDEFINED;
					}
				}
			}
			else
			{
				scanner.unread();
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
		else 
			{
			scanner.unread();
			return Token.UNDEFINED;
			}
	}
	private int readDigits (ICharacterScanner scanner) {
		int length = 0;
		while (isDigit(scanner.read())) length++;
		scanner.unread();
		return length;
	}
	private boolean isDigit (int c) {
		return (c >= '0' && c <= '9');
	}
	
}