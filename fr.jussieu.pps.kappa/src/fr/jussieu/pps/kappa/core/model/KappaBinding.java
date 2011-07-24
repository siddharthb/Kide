package fr.jussieu.pps.kappa.core.model;

import fr.jussieu.pps.kappa.core.parse.ast.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java_cup.runtime.Symbol;

import org.eclipse.jface.text.Region;

/**
 * Represents a bound identifier in an SML program.
 * This is what gets shown in the outline view.
 * <p>
 * A binding can have any number of children. The type of a binding
 * identifies what kind of a binding it is, e.g. a value variable,
 * a datatype, a structure, etc.
 * <p>
 * The SmlBinding class also provides a static method, getBindings(),
 * to extract the bindings from the parse tree of an SML program.
 * This is used only by the SmlProgram itself. Top-level 'use' calls
 * are also included, as long as they are simply of the form
 * 'use("filename")'.
 */
public class KappaBinding {

	private String type;
	private String ident;
	private int left, right;
	private List children;
	private KappaBinding parent;
	
	/** The constructor is private.
	 * Use getBindings(ASTRoot) instead. */
	private KappaBinding (String type, String ident, AST node) {
		this.type = type;
		this.ident = ident;
		left = node.left;
		right = node.right;
		children = new ArrayList();
		parent = null;
	}
	
	private void setChildren (List children) {
		this.children = children;
		for (Iterator i = children.iterator(); i.hasNext();) {
			((KappaBinding) i.next()).setParent(this);
		}
	}
	
	private void setParent (KappaBinding parent) {
		this.parent = parent;
	}
	/** Returns the type of the binding. The returned value is
	 * one of the fields VAL, TYPE, CON, EXN, USE, OPEN,
	 * SIG, STR, FCT and FUNSIG. */
	public String getType () {return type;}
	/** Returns the identifier bound with this binding. */
	public String getIdent () {return ident;}
	/** Returns the offset in the document where this binding begins. */
	public int getLeft () {return left;}
	/** Returns the offset in the document where this binding ends. */
	public int getRight () {return right;}
	/** Returns whether or not this binding has children. */
	public boolean hasChildren () {return !children.isEmpty();}
	/** Returns the children of this binding.
	 * A datatype binding's children are its constructors;
	 * a module's children are the fields it contains;
	 * other types of bindings don't have children. */
	public KappaBinding[] getChildren () {return (KappaBinding[]) children.toArray(new KappaBinding[0]);}
	/** Returns the binding which this binding is a child of,
	 * or null if this is a top-level binding. */
	public KappaBinding getParent () {return parent;}
	
	/* Better not change the values of the strings;
	 * SmlBindingsLabelProvider uses them to get the file name of the
	 * icon displayed in the outline view. */
	/** Identifies a value variable. */
	public static final String AGE = "agent";
	/** Identifies a datatype. */
	public static final String RUL = "rule";
	/** Identifies a constructor. */
	public static final String VAR = "variable";
	/** Identifies an exception. */
	public static final String OBS = "observable";
	/** Identifies a use declaration. */
	public static final String INI = "init";
	/** Identifies an open declaration. */
	public static final String PLO = "plot";
	/** Identifies a signature. */
	public static final String PER = "pert";
	/** Identifies a structure. */
//	public static final String STR = "str";
	/** Identifies a functor. */
//	public static final String FCT = "fct";
	/** Identifies a functor signature. */
//	public static final String FUNSIG = "funsig";
	
	/** Returns all the bindings found in the given parse tree. */
	public static List getBindings (ASTRoot parseTree) {
		List bindings = new ArrayList();
		for (Iterator i = parseTree.sig.iterator(); i.hasNext(); ) {
			Signatures s=(Signatures) i.next();
			bindings.add(SigHelper.getsigBindings(s));
		}	
		//	Symbol error = (Symbol) i.next();
		//	problems.add(new Region(error.left,error.right-error.left));
		for (Iterator i = parseTree.var.iterator(); i.hasNext(); ) {
			Variables s=(Variables) i.next();
			bindings.add(VarHelper.getvarBindings(s));
		}
		for (Iterator i = parseTree.rul.iterator(); i.hasNext(); ) {
			Rules s=(Rules) i.next();
			bindings.add(RuleHelper.getrulBindings(s));
		}
		int j=0;
		for (Iterator i = parseTree.ini.iterator(); i.hasNext(); ) {
			j++;
			Init s=(Init) i.next();
			bindings.add(IniHelper.getiniBindings(s,j));
		}
		for (Iterator i = parseTree.obs.iterator(); i.hasNext(); ) {
			Observables s=(Observables) i.next();
			bindings.add(ObsHelper.getobsBindings(s));
		}
		j=0;
		for (Iterator i = parseTree.per.iterator(); i.hasNext(); ) {
			j++;
			pertubation s=(pertubation) i.next();
			bindings.add(PerHelper.getperBindings(s,j));
		}
		
	//	for (int i = 0; i < parseTree.sig.size(); i++)
	//		bindings.addAll(SigHelper.getsigBindings(s));
		return bindings;
	}
	
	/* These helper classes extract SmlBindings from the AST. */
	private static class SigHelper{
		public static KappaBinding getsigBindings(Signatures s)
		{
			return (new KappaBinding(AGE,s.a.ag_nme,s));
		}
	}
	private static class VarHelper{
		public static KappaBinding getvarBindings(Variables s)
		{
			
			if (s instanceof VAR_KAPPA) return (new KappaBinding(VAR,((VAR_KAPPA) s).v3,s));
			if (s instanceof VAR_ALG) return (new KappaBinding(VAR,((VAR_ALG) s).v3,s));
			return (new KappaBinding(VAR,"some variable",new Variables()));
		}
	}
	private static class RuleHelper{
		public static KappaBinding getrulBindings(Rules s)
		{
			return (new KappaBinding(RUL,s.v1.v5,s));
		}
	}
	private static class IniHelper{
		public static KappaBinding getiniBindings(Init s,int j)
		{
			return (new KappaBinding(INI,"init:".concat(Integer.toString(j)),s));
		}
	}
	private static class ObsHelper{
		public static KappaBinding getobsBindings(Observables s)
		{
			return (new KappaBinding(OBS,((OBS_VAR)s.a).v1,s));
		}
	}
	private static class PerHelper{
		public static KappaBinding getperBindings(pertubation s,int j)
		{
			return (new KappaBinding(PER,"pertubation:".concat(Integer.toString(j)),s));
		}
	}
	/*
	private static class DecHelper {
	
		public static List getDecBindings (Dec dec) {
			if (dec instanceof AbstypeDec) return getAbstypeBindings((AbstypeDec)dec);
			if (dec instanceof DatatypeDec) return getDatatypeBindings((DatatypeDec)dec);
			if (dec instanceof ExnDec) return getExnBindings((ExnDec)dec);
			if (dec instanceof ExpDec) return getExpBindings((ExpDec)dec);
			if (dec instanceof FctDec) return getFctBindings((FctDec)dec);
			if (dec instanceof FunDec) return getFunBindings((FunDec)dec);
			if (dec instanceof FunsigDec) return getFunsigBindings((FunsigDec)dec);
			if (dec instanceof LocalDec) return getLocalBindings((LocalDec)dec);
			if (dec instanceof OpenDec) return getOpenBindings((OpenDec)dec);
			if (dec instanceof RecValDec) return getRecValBindings((RecValDec)dec);
			if (dec instanceof SigDec) return getSigBindings((SigDec)dec);
			if (dec instanceof StrDec) return getStrBindings((StrDec)dec);
			if (dec instanceof TypeDec) return getTypeBindings((TypeDec)dec);
			if (dec instanceof ValDec) return getValBindings((ValDec)dec);
			return new ArrayList();
		}
	
		private static List getAbstypeBindings (AbstypeDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getDatatypeBinding(dec.bindings[i],true));
			for (int i = 0; i < dec.typebinds.length; i++)
				bindings.add(BindHelper.getTypeBinding(dec.typebinds[i]));
			for (int i = 0; i < dec.decs.length; i++)
				bindings.addAll(getDecBindings(dec.decs[i]));
			return bindings;
		}
	
		private static List getDatatypeBindings (DatatypeDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getDatatypeBinding(dec.bindings[i],false));
			for (int i = 0; i < dec.typebinds.length; i++)
				bindings.add(BindHelper.getTypeBinding(dec.typebinds[i]));
			return bindings;
		}
	
		private static List getExnBindings (ExnDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getExnBinding(dec.bindings[i]));
			return bindings;
		}
		
		private static List getExpBindings (ExpDec dec) {
			List bindings = new ArrayList();
			try {
				FlatAppExp exp = (FlatAppExp) dec.exp;
				Ident fn = ((VarExp) exp.exps[0]).ident;
				if (fn.name.equals("use")) {
					StringExp fileExp = (StringExp) exp.exps[1];
					Ident fileIdent = new Ident(fileExp.value);
					fileIdent.mark(fileExp.left,fileExp.right);
					bindings.add(new SmlBinding(USE, fileIdent, dec));
				}
			} catch (ClassCastException e) {
			}
			return bindings;
		}
	
		private static List getFctBindings (FctDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getFctBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getFunBindings (FunDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getFunBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getFunsigBindings (FunsigDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getFunsigBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getLocalBindings (LocalDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.decs.length; i++)
				bindings.addAll(getDecBindings(dec.decs[i]));
			return bindings;
		}
	
		private static List getOpenBindings (OpenDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.idents.length; i++)
				bindings.add(new SmlBinding(OPEN, dec.idents[i], dec));
			return bindings;
		}
	
		private static List getRecValBindings (RecValDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getRecValBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getSigBindings (SigDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getSigBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getStrBindings (StrDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getStrBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getTypeBindings (TypeDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.add(BindHelper.getTypeBinding(dec.bindings[i]));
			return bindings;
		}
	
		private static List getValBindings (ValDec dec) {
			List bindings = new ArrayList();
			for (int i = 0; i < dec.bindings.length; i++)
				bindings.addAll(BindHelper.getValBinding(dec.bindings[i]));
			return bindings;
		}
		
	}

	private static class BindHelper {
	
		public static SmlBinding getConBinding (ConBind cb) {
			return new SmlBinding(CON, cb.ident, cb);
		}
		
		public static SmlBinding getExnBinding (ExnBind eb) {
			return new SmlBinding(EXN, eb.ident, eb);
		}
	
		public static SmlBinding getDatatypeBinding (DatatypeBind db, boolean abs) {
			SmlBinding binding = new SmlBinding(TYPE, db.ident, db);
			if (!abs) {
				List cons = new ArrayList();
				for (int i = 0; i < db.cons.length; i++)
					cons.add(getConBinding(db.cons[i]));
				binding.setChildren(cons);
			}
			return binding;
		}
		
		public static SmlBinding getFctBinding (FctBind fb) {
			SmlBinding binding = new SmlBinding(FCT, fb.ident, fb);
			binding.setChildren(ModuleHelper.getStrBindings(fb.str));
			return binding;
		}
		
		public static SmlBinding getFunBinding (FunBind fb) {
			return new SmlBinding(VAL, PatHelper.getIdentOfFun(fb), fb); 
		}
		
		public static SmlBinding getFunsigBinding (FunsigBind fb) {
			SmlBinding binding = new SmlBinding(FUNSIG, fb.ident, fb);
			binding.setChildren(ModuleHelper.getSigBindings(fb.sig));
			return binding;
		}
		
		public static SmlBinding getRecValBinding (RecValBind rvb) {
			return new SmlBinding(VAL, rvb.ident, rvb);
		}
		
		public static SmlBinding getSigBinding (SigBind sb) {
			SmlBinding binding = new SmlBinding(SIG, sb.ident, sb);
			binding.setChildren(ModuleHelper.getSigBindings(sb.sig));
			return binding;
		}
		
		public static SmlBinding getStrBinding (StrBind sb) {
			SmlBinding binding = new SmlBinding(STR, sb.ident, sb);
			binding.setChildren(ModuleHelper.getStrBindings(sb.str));
			return binding;
		}
		
		public static SmlBinding getTypeBinding (TypeBind tb) {
			return new SmlBinding(TYPE, tb.ident, tb);
		}
		
		public static List getValBinding (ValBind vb) {
			List bindings = new ArrayList();
			List idents = PatHelper.getPatIdents(vb.pat);
			for (Iterator i = idents.iterator(); i.hasNext();)
				bindings.add(new SmlBinding(VAL, (Ident) i.next(), vb));
			return bindings;
		}
	
	}

	private static class PatHelper {
	
		public static List getPatIdents (Pat pat) {
			if (pat instanceof FlatConPat) return getFlatConIdents((FlatConPat)pat);
			if (pat instanceof LayeredPat) return getLayeredIdents((LayeredPat)pat);
			if (pat instanceof ListPat) return getListIdents((ListPat)pat);
			if (pat instanceof OrPat) return getOrIdents((OrPat)pat);
			if (pat instanceof RecordPat) return getRecordIdents((RecordPat)pat);
			if (pat instanceof TuplePat) return getTupleIdents((TuplePat)pat);
			if (pat instanceof TypedPat) return getTypedIdents((TypedPat)pat);
			if (pat instanceof VarPat) return getVarIdents((VarPat)pat);
			if (pat instanceof VectorPat) return getVectorIdents((VectorPat)pat);
			return new ArrayList();
		}
		
		private static List getFlatConIdents (FlatConPat pat) {
			return getPatIdents(pat.pats[pat.pats.length-1]);
		}
		
		private static List getLayeredIdents (LayeredPat pat) {
			List idents = getPatIdents(pat.pat1);
			idents.addAll(getPatIdents(pat.pat2));
			return idents;
		}
		
		private static List getListIdents (ListPat pat) {
			List idents = new ArrayList();
			for (int i = 0; i < pat.pats.length; i++) {
				idents.addAll(getPatIdents(pat.pats[i]));
			}
			return idents;
		}
		
		private static List getOrIdents (OrPat pat) {
			List idents = new ArrayList();
			for (int i = 0; i < pat.pats.length; i++) {
				idents.addAll(getPatIdents(pat.pats[i]));
			}
			return idents;
		}
		
		private static List getRecordIdents (RecordPat pat) {
			List idents = new ArrayList();
			for (int i = 0; i < pat.fields.length; i++) {
				Field field = pat.fields[i];
				if (pat.fields[i] instanceof PatField)
					idents.addAll(getPatIdents(((PatField)field).pat));
				if (pat.fields[i] instanceof VarPatField) {
					idents.add(((VarPatField)field).ident);
					if (((VarPatField)field).aspat != null)
						idents.addAll(getPatIdents(((VarPatField)field).aspat));
				}
			}
			return idents;
		}
		
		private static List getTupleIdents (TuplePat pat) {
			List idents = new ArrayList();
			for (int i = 0; i < pat.pats.length; i++) {
				idents.addAll(getPatIdents(pat.pats[i]));
			}
			return idents;
		}
		
		private static List getTypedIdents (TypedPat pat) {
			return getPatIdents(pat.pat);
		}
		
		private static List getVarIdents (VarPat pat) {
			List idents = new ArrayList();
			idents.add(pat.ident);
			return idents;
		}
		
		private static List getVectorIdents (VectorPat pat) {
			List idents = new ArrayList();
			for (int i = 0; i < pat.pats.length; i++) {
				idents.addAll(getPatIdents(pat.pats[i]));
			}
			return idents;
		}
	
		public static Ident getIdentOfFun (FunBind fb) {
			return (Ident) getPatIdents(fb.clauses[0].pats[0]).get(0);
		}
		
	}

	private static class ModuleHelper {
	
		public static List getStrBindings (Str str) {
			if (str instanceof BaseStr) {
				List children = new ArrayList();
				for (int i = 0; i < ((BaseStr)str).decs.length; i++)
					children.addAll(DecHelper.getDecBindings(((BaseStr)str).decs[i]));
				return children;
			}
			if (str instanceof LetStr)
				return getStrBindings(((LetStr)str).str);
			if (str instanceof SignedStr)
				return getStrBindings(((SignedStr)str).str);
			return new ArrayList();
		}
		
		public static List getSigBindings (Sig sig) {
			if (sig instanceof BaseSig) {
				List children = new ArrayList();
				for (int i = 0; i < ((BaseSig)sig).specs.length; i++)
					children.addAll(SpecHelper.getSpecBindings(((BaseSig)sig).specs[i]));
				return children;
			}
			if (sig instanceof WhereSig)
				return getSigBindings(((WhereSig)sig).sig);
			return new ArrayList();
		}
	
	}
	
	private static class SpecHelper {
		
			public static List getSpecBindings (Spec spec) {
				if (spec instanceof DatatypeSpec) return getDatatypeBindings((DatatypeSpec)spec);
				if (spec instanceof ExnSpec) return getExnBindings((ExnSpec)spec);
				if (spec instanceof FctSpec) return getFctBindings((FctSpec)spec);
				if (spec instanceof IncludeSpec) return getIncludeSpecBindings((IncludeSpec)spec);
				if (spec instanceof StrSpec) return getStrBindings((StrSpec)spec);
				if (spec instanceof TypeSpec) return getTypeBindings((TypeSpec)spec);
				if (spec instanceof ValSpec) return getValBindings((ValSpec)spec);
				return new ArrayList();
			}
		
			private static List getDatatypeBindings (DatatypeSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.bindings.length; i++)
					bindings.add(BindHelper.getDatatypeBinding(spec.bindings[i],false));
				for (int i = 0; i < spec.typebinds.length; i++)
					bindings.add(BindHelper.getTypeBinding(spec.typebinds[i]));
				return bindings;
			}
		
			private static List getExnBindings (ExnSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.descs.length; i++)
					bindings.add(DescHelper.getExnBinding(spec.descs[i]));
				return bindings;
			}
		
			private static List getFctBindings (FctSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.descs.length; i++)
					bindings.add(DescHelper.getFctBinding(spec.descs[i]));
				return bindings;
			}
		
			private static List getIncludeSpecBindings (IncludeSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.idents.length; i++)
					bindings.add(new SmlBinding(OPEN, spec.idents[i], spec));
				return bindings;
			}
		
			private static List getStrBindings (StrSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.descs.length; i++)
					bindings.add(DescHelper.getStrBinding(spec.descs[i]));
				return bindings;
			}
		
			private static List getTypeBindings (TypeSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.descs.length; i++)
					bindings.add(DescHelper.getTypeBinding(spec.descs[i]));
				return bindings;
			}
		
			private static List getValBindings (ValSpec spec) {
				List bindings = new ArrayList();
				for (int i = 0; i < spec.descs.length; i++)
					bindings.add(DescHelper.getValBinding(spec.descs[i]));
				return bindings;
			}
			
		}
	
	private static class DescHelper {
		
		public static SmlBinding getExnBinding (ExnDesc eb) {
			return new SmlBinding(EXN, eb.ident, eb);
		}
	
		public static SmlBinding getFctBinding (FctDesc fb) {
			return new SmlBinding(FCT, fb.ident, fb);
		}
		
		public static SmlBinding getStrBinding (StrDesc sb) {
			SmlBinding binding = new SmlBinding(STR, sb.ident, sb);
			binding.setChildren(ModuleHelper.getSigBindings(sb.sig));
			return binding;
		}
		
		public static SmlBinding getTypeBinding (TypeDesc tb) {
			return new SmlBinding(TYPE, tb.ident, tb);
		}
		
		public static SmlBinding getValBinding (ValDesc vb) {
			return new SmlBinding(VAL, vb.ident, vb);
		}
		
	}
	*/
}