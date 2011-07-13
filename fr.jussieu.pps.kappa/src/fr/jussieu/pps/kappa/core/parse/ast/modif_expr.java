package fr.jussieu.pps.kappa.core.parse.ast;

public abstract class modif_expr extends AST {
	public modif_expr mark (int l, int r) {super.setPos(l,r); return this;}
}