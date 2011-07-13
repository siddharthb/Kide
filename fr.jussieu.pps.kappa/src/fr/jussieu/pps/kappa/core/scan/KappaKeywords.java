package fr.jussieu.pps.kappa.core.scan;

import fr.jussieu.pps.kappa.core.parse.KappaSymbols;

import java.util.HashMap;
import java.util.Map;

/** A utility class for recognizing SML keywords.
 * Used by the lexer and for syntax highlighting. */
public class KappaKeywords {

	/** Internal mapping from strings to Integers for getSym(). */
	private static Map<String,Integer> symMap;
	
	/** Returns the symbol number that identifies what kind of token it is.
	 * Used by SmlLexer for passing to SmlParser. */
	public static int getSym (String word) {
		Integer sym = (Integer) getMap().get(word);
		if (sym == null) return KappaSymbols.ID;
		else return sym.intValue();
	}
	
	/** Returns the list of known SML keywords. */
	public static String[] getKeywords () {
		return (String[]) getMap().keySet().toArray(new String[0]);
	}
	
	private static Map<String,Integer> getMap () {
		if (symMap == null)
			symMap = createMap();
		return symMap;
	}

	private static Map<String,Integer> createMap () {
		Map<String,Integer> map = new HashMap<String,Integer>();
		
		map.put("^", new Integer(KappaSymbols.POW));
		map.put("&&", new Integer(KappaSymbols.AND));
		map.put("[sqrt]", new Integer(KappaSymbols.SQRT));
		map.put("[int]", new Integer(KappaSymbols.ABS));
		map.put("*", new Integer(KappaSymbols.MULT));
		map.put("[E]", new Integer(KappaSymbols.EVENT));
		map.put("do", new Integer(KappaSymbols.DO));
		map.put("?", new Integer(KappaSymbols.KAPPA_WLD));
		map.put("_", new Integer(KappaSymbols.KAPPA_SEMI));
		map.put("->", new Integer(KappaSymbols.KAPPA_RAR));
		map.put("[T]", new Integer(KappaSymbols.TIME));
		map.put("<", new Integer(KappaSymbols.SMALLER));
		map.put(">", new Integer(KappaSymbols.GREATER));
		map.put("%init:", new Integer(KappaSymbols.INIT));
		map.put("%agent:", new Integer(KappaSymbols.SIGNATURE));
		map.put("%var:", new Integer(KappaSymbols.LET));
		map.put("%obs:", new Integer(KappaSymbols.OBS));
		map.put("%mod:", new Integer(KappaSymbols.PERT));
		map.put("%plot:", new Integer(KappaSymbols.PLOT));
		map.put("%ref:", new Integer(KappaSymbols.REF));
		map.put("[log]", new Integer(KappaSymbols.LOG));
		map.put("[sin]", new Integer(KappaSymbols.SINUS));
		map.put("[cos]", new Integer(KappaSymbols.COSINUS));
		map.put("[tan]", new Integer(KappaSymbols.TAN));
		map.put("[exp]", new Integer(KappaSymbols.EXPONENT));
		map.put("[mod]", new Integer(KappaSymbols.MODULO));
		map.put("$DEL", new Integer(KappaSymbols.DELETE));
		map.put("$ADD", new Integer(KappaSymbols.INTRO));
		map.put("$SNAPSHOT", new Integer(KappaSymbols.SNAPSHOT));
		map.put("$STOP", new Integer(KappaSymbols.STOP));
		map.put("[inf]", new Integer(KappaSymbols.INFINITY));
		map.put("[true]", new Integer(KappaSymbols.TRUE));
		map.put("[false]", new Integer(KappaSymbols.FALSE));
		map.put("[not]", new Integer(KappaSymbols.NOT));
		map.put("[emax]", new Integer(KappaSymbols.EMAX));
		map.put("[tmax]", new Integer(KappaSymbols.TMAX));
		map.put("until", new Integer(KappaSymbols.UNTIL));
		map.put(":=", new Integer(KappaSymbols.SET));
		map.put("||", new Integer(KappaSymbols.OR));
		map.put("->!", new Integer(KappaSymbols.KAPPA_NOPOLY));
		map.put("@", new Integer(KappaSymbols.AT));
		map.put("|", new Integer(KappaSymbols.PIPE));
		map.put("+", new Integer(KappaSymbols.PLUS));

		map.put("(", new Integer(KappaSymbols.OP_PAR));
		map.put(")", new Integer(KappaSymbols.CL_PAR));
		map.put("-", new Integer(KappaSymbols.MINUS));
		map.put("/", new Integer(KappaSymbols.DIV));
		map.put("=", new Integer(KappaSymbols.EQUAL));
		map.put("!", new Integer(KappaSymbols.KAPPA_LNK));
		map.put(".", new Integer(KappaSymbols.DOT));
		map.put(",", new Integer(KappaSymbols.COMMA));
		map.put("\n", new Integer(KappaSymbols.NEWLINE));
		return map;
	}
	
	/** There's no point in instantiating this class. */
	private KappaKeywords () {}
	
}