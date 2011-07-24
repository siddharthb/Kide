package fr.jussieu.pps.kappa.core.parse.ast;
import java.util.List;
import java.util.ArrayList;
/** The root of the parse tree.
 * Its children are the top-level declarations in the program. */
public class ASTRoot extends AST {
	public List<Variables> var;
	public List<Signatures> sig;
	public List<Rules> rul;
	public List<Observables> obs;	
	public List<Init> ini;
	public List<pertubation> per;
	public ASTRoot () {var = new ArrayList<Variables>();
	sig = new ArrayList<Signatures>();
	rul = new ArrayList<Rules>();
	obs = new ArrayList<Observables>();
	per = new ArrayList<pertubation>();
	ini = new ArrayList<Init>();}
/*	public ASTRoot (java.util.List<Variables> v,java.util.List<Signatures> s,java.util.List<Rules> r,java.util.List<Init> i) 
	{var = (Variables[]) v.toArray(new Variables[0]);
	sig = (Signatures[]) s.toArray(new Signatures[0]);
	rul = (Rules[]) r.toArray(new Rules[0]);
	ini = (Init[]) i.toArray(new Init[0]);}
*/	
	public void addVar(Variables v){var.add(v);}
	public void addSig(Signatures s){sig.add(s);}
	public void addRul(Rules r){rul.add(r);}
	public void addInit(Init i){ini.add(i);}
	public void addObs(Observables i){obs.add(i);}
	public void addPer(pertubation i){per.add(i);}
	public ASTRoot mark (int l, int r) {super.setPos(l,r); return this;}
}
