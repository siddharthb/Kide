package fr.jussieu.pps.kappa.core.parse.ast;

public class pertubation extends AST {
	public bool_expr v11;
	public modif_expr v12;
	public bool_expr v3;
	public pertubation(bool_expr f1,modif_expr f2){v11=f1;v12=f2;}
	public pertubation(bool_expr f1,modif_expr f2,bool_expr f3){v11=f1;v12=f2;v3=f3;}
	public pertubation mark (int l, int r) {super.setPos(l,r); return this;}
}