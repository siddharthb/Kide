package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class alg_expr extends AST {
	public alg_expr mark (int l, int r) {super.setPos(l,r); return this;}
}