package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class bool_expr extends AST {
	public bool_expr mark (int l, int r) {super.setPos(l,r); return this;}
}