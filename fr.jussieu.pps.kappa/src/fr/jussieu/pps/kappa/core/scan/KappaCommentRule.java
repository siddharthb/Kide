package fr.jussieu.pps.kappa.core.scan;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/** A rule that matches an Kappa comment, which may be nested.
 * Used by KappaTokenScanner. */
public class KappaCommentRule implements IRule {
	private IToken token;
	public KappaCommentRule (IToken successToken) {token = successToken;}
	public IToken evaluate (ICharacterScanner scanner) {
		if (scanner.read() != '#') {scanner.unread(); return Token.UNDEFINED;}
		int depth = 1;
		while (depth > 0) {
			switch (scanner.read()) {
			case '\n':
			{
				scanner.unread();
				return token;
			}
			case ICharacterScanner.EOF:
				return token;
			}
		}
		return token;
	}
}