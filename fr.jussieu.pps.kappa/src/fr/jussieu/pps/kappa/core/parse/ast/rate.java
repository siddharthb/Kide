package fr.jussieu.pps.kappa.core.parse.ast;

public class rate extends AST {
	public alg_expr v11;
	public alg_expr v12;
	public rate(alg_expr f1){v11=f1;}
	public rate(alg_expr f1,alg_expr f2){v11=f1;v12=f2;}
	public rate mark (int l, int r) {super.setPos(l,r); return this;}
}