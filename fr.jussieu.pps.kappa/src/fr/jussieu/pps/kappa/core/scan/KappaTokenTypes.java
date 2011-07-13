package fr.jussieu.pps.kappa.core.scan;

/** A utility class containing identifiers of the different kinds of tokens
 * that can be returned by an KappaTokenScanner or an KappaPartitionScanner. */
public class KappaTokenTypes {

	/** Identifies an SML identifier or keyword. */
	public static final String WORD = "fr.jussieu.pps.kappa.kappaWord";

	/** Identifies an SML type variable (the "'a" in "'a list"). */
//	public static final String TYVAR = "fr.jussieu.pps.kappa.kappaTyvar";
	
	/** Identifies an SML integer literal. */
	
	public static final String INT = "fr.jussieu.pps.kappa.kappaInt";
	/** Identifies an SML real literal. */
	public static final String REAL = "fr.jussieu.pps.kappa.kappaReal";
	
	/** Identifies an SML string literal. */
	public static final String LABEL = "fr.jussieu.pps.kappa.kappaLabel";
	
	/** Identifies an Kappa comment. */
	public static final String COMMENT = "fr.jussieu.pps.kappa.kappaComment";

	/** There's no point in instantiating this class. */
	private KappaTokenTypes () {}

}
